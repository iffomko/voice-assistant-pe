package com.iffomko.voiceAssistant.security.jwt;

import com.iffomko.voiceAssistant.db.entities.Role;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

/**
 * Обеспечивает логику по взаимодействую с JWT токеном
 */
@Component
public class JwtTokenProvider {
    private final UserDetailsService userDetailsService;

    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${jwt.authorizationHeader}")
    private String authorizationHeader;
    @Value("${jwt.validityInMilliseconds}")
    private long validityInMilliseconds;
    @Value("${jwt.issuer}")
    private String issuer;

    /**
     * @param userDetailsService сервис по работе с пользователями
     */
    @Autowired
    public JwtTokenProvider(@Qualifier("userDetailsServiceDao") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Этот метод вызывает для инициализации бина на этапе конфигурации.
     * Здесь мы кодируем секретный ключ для шифрования JWT токена
     */
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    /**
     * Создает токен для какого-то пользователя по его username и роли
     * @param username username пользователя
     * @param role его роль
     * @return JWT токен для конкретного пользователя
     */
    public String createToken(String username, Role role) {
        Claims claims = Jwts.claims();
        claims.setSubject(username);
        claims.put("role", role);

        Date currentMoment = new Date();
        Date endMoment = new Date(currentMoment.getTime() + validityInMilliseconds * 1000);

        return Jwts.builder()
                .addClaims(claims)
                .setIssuedAt(currentMoment)
                .setExpiration(endMoment)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .setIssuer(issuer)
                .compact();
    }

    /**
     * Проверяет JWT токен на корректность
     * @param token сам токен
     * @return возвращает true если токен валидный или false, если нет
     * @throws JwtAuthenticationException выбрасывается если время токена вышла или он не валидный
     */
    public boolean validateToken(String token) throws JwtAuthenticationException {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("Token is expiration or not valid", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Возвращает аутентификацию для пользователя по его JWT токену
     * @param token сам токен
     * @return аутентификация пользователя
     */
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));

        return new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                "",
                userDetails.getAuthorities()
        );
    }

    /**
     * Возвращает username пользователя из JWT токена
     * @param token сам токен
     * @return username пользователя
     */
    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Возвращает токен из соответствующего заголовка в HTTP запросе клиента
     * @param request HTTP запрос клиента
     * @return JWT токен
     */
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader(authorizationHeader);
    }
}
