package com.iffomko.voiceAssistant.security;

import com.iffomko.voiceAssistant.db.entities.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
public class SecurityUser implements UserDetails {
    private final String username;
    private final String password;
    private final List<SimpleGrantedAuthority> authorities;
    private final boolean isActive;

    /**
     * Инициализирует нужные поля
     * @param username username пользователя
     * @param password пароль пользователя
     * @param authorities список предоставленных прав пользователю
     */
    public SecurityUser(String username, String password, List<SimpleGrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.isActive = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    /**
     * Конвертирует сущностью из базы данных <code>User</code> в класс <code>SecurityUser</code>
     * @param user сущность из базы данных
     * @return объект <code>SecurityUser</code>
     */
    public static SecurityUser fromUser(User user) {
        return new SecurityUser(
                user.getUsername(),
                user.getPassword(),
                user.getRole().getAuthorities()
        );
    }
}
