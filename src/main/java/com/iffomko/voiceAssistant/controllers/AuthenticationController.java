package com.iffomko.voiceAssistant.controllers;

import com.iffomko.voiceAssistant.controllers.data.AuthenticationRequestDTO;
import com.iffomko.voiceAssistant.controllers.data.AuthenticationResponseDTO;
import com.iffomko.voiceAssistant.controllers.data.RegistrationRequestDTO;
import com.iffomko.voiceAssistant.controllers.data.RegistrationResponseDTO;
import com.iffomko.voiceAssistant.controllers.errors.AuthenticationError;
import com.iffomko.voiceAssistant.db.entities.Role;
import com.iffomko.voiceAssistant.db.entities.User;
import com.iffomko.voiceAssistant.db.services.dao.UserDao;
import com.iffomko.voiceAssistant.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер аутентификации. Именно здесь происходит авторизация и регистрация пользователей
 */
@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final UserDao userDao;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    /**
     * Конструктор, который инициализирует нужные поля объектами
     * @param authenticationManager менеджер аутентификации
     * @param userDao DAO объект, который отвечает за работу с пользователями (сущность <code>User</code>)
     * @param jwtTokenProvider объект <code>JwtTokenProvider</code>, в нем содержится вся основная логика по работе
     *                         с JWT токеном
     * @param passwordEncoder любой объект реализующий интерфейс <code>PasswordEncoder</code>
     */
    @Autowired
    public AuthenticationController(
            @Qualifier("authenticationManager") AuthenticationManager authenticationManager,
            UserDao userDao,
            JwtTokenProvider jwtTokenProvider,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Эндпоинт, который отвечает за авторизацию пользователя в системе
     * @param body объект <code>AuthenticationRequestDTO</code>, который содержит данные пользователя для входа в систему
     * @return ответ <code>ResponseEntity<AuthenticationResponseDTO></code>
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(@RequestBody AuthenticationRequestDTO body) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(body.getUsername(), body.getPassword())
            );

            User user = userDao.findUserByUsername(body.getUsername());
            String jwtToken = jwtTokenProvider.createToken(
                    user.getUsername(),
                    user.getRole()
            );

            return ResponseEntity.ok(new AuthenticationResponseDTO(user.getUsername(), jwtToken, null));
        } catch (AuthenticationException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new AuthenticationResponseDTO(
                            null,
                            null,
                                new AuthenticationError("Вы не вошли в систему")
                    )
            );
        }
    }

    /**
     * Эндпоинт, который позволяет выйти со системы (по сути очищает SecurityContext и делает сессию не валидной)
     * @param request сервлет запроса клиента
     * @param response сервлет ответа для клиента
     */
    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }

    /**
     * Регистрирует пользователя в системе
     * @param body объект <code>RegistrationRequestDTO</code>, который содержит данные для регистрации пользователя
     * @return объект <code>ResponseEntity<RegistrationResponseDTO></code> ответа пользователю
     */
    @PostMapping("/register")
    public ResponseEntity<RegistrationResponseDTO> register(@RequestBody RegistrationRequestDTO body) {
        User user = userDao.findUserByUsername(body.getUsername());

        if (user != null) {
            return ResponseEntity.badRequest().body(new RegistrationResponseDTO(
                    HttpStatus.BAD_REQUEST.value(),
                    "Вы уже зарегистрированы в системе"
            ));
        }

        user = new User();
        user.setUsername(body.getUsername());
        user.setPassword(passwordEncoder.encode(body.getPassword()));
        user.setName(body.getName());
        user.setSurname(body.getSurname());
        user.setRole(Role.USER);

        userDao.addUser(user);

        return ResponseEntity.ok(
                new RegistrationResponseDTO(HttpStatus.OK.value(), "Вы были успешно зарегистрированы")
        );
    }
}
