package com.iffomko.voiceAssistant.APIs.openAI.data;

import lombok.Data;

/**
 * <p>Объект, который хранит в себе количество токенов вопроса, ответа и их сумму</p>
 */
@Data
public class OpenAIUsage {
    private Integer promptTokens;
    private Integer completionTokens;
    private Integer totalTokens;

    public OpenAIUsage() {}

    public OpenAIUsage(Integer promptTokens, Integer completionTokens, Integer totalTokens) {
        this.promptTokens = promptTokens;
        this.completionTokens = completionTokens;
        this.totalTokens = totalTokens;
    }
}
