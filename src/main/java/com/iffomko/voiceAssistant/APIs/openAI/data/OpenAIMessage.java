package com.iffomko.voiceAssistant.APIs.openAI.data;

import lombok.Data;

/**
 * <p>Объект, который хранит в себе информацию о сообщении в ответе на вопрос от OpenAI API</p>
 */
@Data
public class OpenAIMessage {
    private String role;
    private String content;

    public OpenAIMessage() {}

    public OpenAIMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }
}
