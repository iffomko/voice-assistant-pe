package com.iffomko.voiceAssistant.APIs.openAI.types;

/**
 * <p>Перечисление всех моделей доступных для обращение к API OpenAI и их "температура"</p>
 */
public enum OpenAIModels {
    CHAT_GPT_3_5("gpt-3.5-turbo", 0.6),
    CHAT_GPT_4("gpt-4", 0.6);

    private final String model;
    private final Double temperature;

    OpenAIModels(String model, Double temperature) {
        this.model = model;
        this.temperature = temperature;
    }

    public String getModel() {
        return model;
    }

    public Double getTemperature() {
        return temperature;
    }
}
