package com.iffomko.voiceAssistant.security;

import com.iffomko.voiceAssistant.db.entities.User;
import com.iffomko.voiceAssistant.db.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Реализует интерфейс для взаимодействия с <code>UserDetails</code> и умеет подгружать пользователя по его username
 */
@Service("userDetailsServiceDao")
public class UserDetailsServiceDao implements UserDetailsService {
    private final UserService userService;

    @Autowired
    public UserDetailsServiceDao(@Qualifier("UserDAO") UserService userService) {
        this.userService = userService;
    }

    /**
     * Подгружает пользователя из базы данных по его username
     * @param username username пользователя
     * @return возвращает найденного пользователя (<code>UserDetails</code>)
     * @throws UsernameNotFoundException выбрасывается если пользователь не существует
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return SecurityUser.fromUser(user);
    }
}
