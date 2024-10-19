package com.iffomko.voiceAssistant.security.jwt;


import org.springframework.http.HttpStatus;

/**
 * Объект ошибки, который используется по работе с JWT токеном
 */
public class JwtAuthenticationException extends RuntimeException {
    private final String message;
    private final HttpStatus status;

    public JwtAuthenticationException(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    /**
     * Возвращает сообщение ошибки
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * Возвращает статус ошибки
     */
    public HttpStatus getStatus() {
        return status;
    }
}
