package com.iffomko.voiceAssistant.APIs.openAI.data;

import lombok.Data;

/**
 * <p>
 *     Объект, который хранит один ответ на вопрос от OpenAI API:
 *     сообщение, причина завершения и индекс в массиве таких ответов (ответов может быть несколько)
 * </p>
 */
@Data
public class OpenAIChoices {
    private OpenAIMessage message;
    private String finishReason;
    private Integer index;

    public OpenAIChoices() {}

    public OpenAIChoices(OpenAIMessage message, String finishReason, Integer index) {
        this.message = message;
        this.finishReason = finishReason;
        this.index = index;
    }
}
