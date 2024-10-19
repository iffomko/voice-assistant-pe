package com.iffomko.voiceAssistant.configs;

import com.iffomko.voiceAssistant.APIs.openAI.AIService;
import com.iffomko.voiceAssistant.APIs.speech.YandexClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.iffomko.voiceAssistant")
public class VoiceAssistantConfig {
    /**
     * Создает бин клиента по работе с YandexAPI
     * @return возвращает этот объект
     */
    @Bean("yandexClient")
    public YandexClient getYandexRecognition() {
        String apiKey = System.getenv("API_KEY");

        return new YandexClient(apiKey);
    }

    /**
     * Создает бин клиента по работе с OpenAI API
     * @return возвращает этот объект
     */
    @Bean("AIService")
    public AIService getAIService() {
        String apiKey = System.getenv("OPEN_AI_API_KEY");

        return new AIService(apiKey);
    }
}
