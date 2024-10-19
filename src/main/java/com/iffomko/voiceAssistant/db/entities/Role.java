package com.iffomko.voiceAssistant.db.entities;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;

/**
 * Перечисление всех ролей
 */
public enum Role {
    /**
     * Роль пользователя
     */
    USER(Set.of(Permissions.GET_ANSWER));

    private final Set<Permissions> permissions;

    Role(Set<Permissions> permissions) {
        this.permissions = permissions;
    }

    /**
     * Возвращает множество всех прав роли
     */
    public Set<Permissions> getPermissions() {
        return permissions;
    }

    /**
     * Возвращает список предоставленных прав пользователей в том виде, в котором удобно Spring Security
     */
    public List<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream().map(
                permission -> new SimpleGrantedAuthority(permission.getPermission())
        ).toList();
    }
}
