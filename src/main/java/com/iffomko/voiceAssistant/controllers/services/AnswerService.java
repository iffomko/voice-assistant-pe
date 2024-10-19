package com.iffomko.voiceAssistant.controllers.services;

import com.iffomko.voiceAssistant.APIs.openAI.AIService;
import com.iffomko.voiceAssistant.APIs.openAI.data.ModelResponse;
import com.iffomko.voiceAssistant.APIs.openAI.data.OpenAIChoices;
import com.iffomko.voiceAssistant.APIs.openAI.types.OpenAIRole;
import com.iffomko.voiceAssistant.APIs.speech.YandexClient;
import com.iffomko.voiceAssistant.APIs.speech.data.RecognitionResponse;
import com.iffomko.voiceAssistant.APIs.speech.types.YandexVoice;
import com.iffomko.voiceAssistant.db.entities.User;
import com.iffomko.voiceAssistant.db.services.UserService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Base64;

/**
 * <p>Класс бизнес-логики формирования ответа на вопрос</p>
 */
@Service
@Component
@Slf4j
@Scope("prototype")
public class AnswerService {
    private final YandexClient yandexClient;
    private final AIService chatGPT;
    private final UserService userService;

    @Autowired
    public AnswerService(
            @Qualifier("yandexClient") YandexClient yandexClient,
            @Qualifier("AIService") AIService chatGPT,
            @Qualifier("UserDAO") UserService userService
    ) {
        this.yandexClient = yandexClient;
        this.chatGPT = chatGPT;
        this.userService = userService;
    }

    /**
     * <p>В параметрах передается звук в формате Base64 и на выходе возвращается текстовое содержание этого звука</p>
     * @param voice - звук в кодировке base64
     * @return - возвращается текст звука или <code>null</code>, если звук не закодирован в Base64
     */
    private String audioToText(String voice, String format, Boolean profanityFilter) {
        byte[] bytes = null;

        try {
            bytes = Base64.getDecoder().decode(voice);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return null;
        }

        if (format != null) {
            yandexClient.getRecognition().setFormat(format);
        }

        if (profanityFilter != null) {
            yandexClient.getRecognition().setProfanityFilter(profanityFilter);
        }

        RecognitionResponse response = yandexClient.recognise(bytes);

        if (response == null) {
            return null;
        }

        return yandexClient.recognise(bytes).getResult();
    }

    /**
     * <p>Переводит текст в звук</p>
     * @param text - текст, который нужно озвучить
     * @param format - формат выходного звука (mp3, oggopus, lpcm)
     * @return - звук, закодированный в Base64
     */
    private String textToVoice(String text, String format) {
        if (format != null) {
            yandexClient.getSynthesis().setFormat(format);
        }

        yandexClient.getSynthesis().setVoice(YandexVoice.FILIPP);

        byte[] voice = yandexClient.synthesis(text);

        if (voice == null) {
            return null;
        }

        return Base64.getEncoder().encodeToString(voice);
    }

    /**
     * <p>Формирует ответ на вопрос пользователя</p>
     * @param username username текущего пользователя
     * @param voice аудио-сообщение, в котором содержится вопрос
     * @param format формат аудио-сообщения
     * @param profanityFilter фильтр
     * @return возвращает ответ на вопрос пользователя в виде аудиосообщения
     */
    public String getAnswer(
            @NonNull String username,
            @NonNull String voice,
            @NonNull String format,
            @NonNull Boolean profanityFilter
    ) {
        User user = userService.findUserByUsername(username);

        if (user == null) {
            return null;
        }

        String text = audioToText(voice, format, profanityFilter);
        ModelResponse response = chatGPT.ask(user.getId(), text);

        if (response == null) {
            return null;
        }

        StringBuilder answerBuilder = new StringBuilder();

        for (OpenAIChoices choice : response.getChoices()) {
            if (choice.getMessage().getRole().equals(OpenAIRole.ASSISTANT.getRole())) {
                answerBuilder.append(choice.getMessage().getContent());
                answerBuilder.append("\n\n");
            }
        }

        String answerChatGPT = answerBuilder.toString();
        
        return textToVoice(answerChatGPT, format);
    }
}
