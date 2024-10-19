package com.iffomko.voiceAssistant.db.entities;

/**
 * Перечисления всех прав
 */
public enum Permissions {
    /**
     * Право на получения ответа
     */
    GET_ANSWER("get:answer");

    private final String permission;

    Permissions(String permission) {
        this.permission = permission;
    }

    /**
     * Возвращает право
     * @return право
     */
    public String getPermission() {
        return permission;
    }
}
