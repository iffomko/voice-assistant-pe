package com.iffomko.voiceAssistant.controllers.data;

import com.iffomko.voiceAssistant.controllers.errors.AuthenticationError;
import lombok.Data;

@Data
public class AuthenticationResponseDTO {
    private String username;
    private String token;
    private AuthenticationError error;

    public AuthenticationResponseDTO() {}

    public AuthenticationResponseDTO(String username, String token, AuthenticationError error) {
        this.username = username;
        this.token = token;
        this.error = error;
    }
}
