package com.iffomko.voiceAssistant.controllers.data;

import lombok.Data;

@Data
public class RegistrationRequestDTO {
    private String username;
    private String name;
    private String surname;
    private String password;
}
