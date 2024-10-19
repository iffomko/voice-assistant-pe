package com.iffomko.voiceAssistant.controllers.errors;

import lombok.Data;

@Data
public class AuthenticationError {
    private String message;

    public AuthenticationError() {}

    public AuthenticationError(String message) {
        this.message = message;
    }
}
