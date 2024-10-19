package com.iffomko.voiceAssistant.controllers.data;

import lombok.Data;

@Data
public class AuthenticationRequestDTO {
    private String username;
    private String password;
}
