package com.iffomko.voiceAssistant.APIs.openAI.types;

/**
 * <p>Перечисление, которое хранит в себе все роли, которые могут быть у сообщения</p>
 */
public enum OpenAIRole {
    USER("user"),
    ASSISTANT("assistant"),
    SYSTEM("system");

    private final String role;

    OpenAIRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
