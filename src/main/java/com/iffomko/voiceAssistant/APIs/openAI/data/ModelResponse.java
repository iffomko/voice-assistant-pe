package com.iffomko.voiceAssistant.APIs.openAI.data;

import lombok.Data;

import java.util.List;

/**
 * <p>Объект, который хранит в себе ответ от OpenAI API</p>
 */
@Data
public class ModelResponse {
    private String id;
    private String object;
    private Integer created;
    private String model;
    private OpenAIUsage usage;
    private List<OpenAIChoices> choices;
}
