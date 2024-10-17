package com.iffomko.voiceAssistant.APIs.openAI.data;

import lombok.Data;

import java.util.List;

/**
 * <p>Объект, который конвертируется в JSON для передачи в теле запроса к OpenAI API</p>
 */
@Data
public class ModelBodyDTO {
    private String model;
    private Double temperature;
    private List<OpenAIMessage> messages;

    public ModelBodyDTO() {}

    public ModelBodyDTO(String model, Double temperature, List<OpenAIMessage> messages) {
        this.model = model;
        this.temperature = temperature;
        this.messages = messages;
    }
}
