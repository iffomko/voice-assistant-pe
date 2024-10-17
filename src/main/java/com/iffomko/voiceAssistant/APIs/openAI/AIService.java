package com.iffomko.voiceAssistant.APIs.openAI;

import com.iffomko.voiceAssistant.APIs.openAI.data.ModelResponse;
import com.iffomko.voiceAssistant.APIs.openAI.data.OpenAIChoices;
import com.iffomko.voiceAssistant.APIs.openAI.data.OpenAIMessage;
import com.iffomko.voiceAssistant.APIs.openAI.types.OpenAIRole;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * <p>
 *     Класс-сервис, который настраивает модель соответствующим образом и хранит в себе контекст пользователя
 *     в течении одного часа, который помогает модели ориентироваться в сообщениях пользователя
 * </p>
 * <p>
 *     Также класс умеет само очищаться если время последнего взаимодействия пользователя
 *     модели с пользователем была более часа назад
 * </p>
 */
@Slf4j
public class AIService {
    private final AIModel model;
    private final Map<Integer, List<OpenAIMessage>> usersMessages;
    private final Map<Integer, Date> modelLastInteraction;

    public AIService(String apiKey) {
        model = new AIModel(apiKey);
        usersMessages = new HashMap<>();
        modelLastInteraction = new HashMap<>();

        new Thread(new ClearMessagesGarber(this, 10000)).start();
    }

    public AIModel getModel() {
        return model;
    }

    /**
     * <p>
     *     Перед запросом настраивает модели если это необходимо и подключается к OpenAI API,
     *     дожидается ответ и возвращает его
     * </p>
     * @param userId id пользователя, который запросил ответ на свой вопрос
     * @param message сам вопрос
     * @return ответ на вопрос, который хранится в объекте типа <code>ModelResponse</code>
     */
    public ModelResponse ask(int userId, String message) {
        synchronized (usersMessages) {
            synchronized (modelLastInteraction) {
                if (usersMessages.get(userId) == null) {
                    usersMessages.put(userId, new ArrayList<>());
                    modelLastInteraction.put(userId, new Date());
                }
            }

            if (usersMessages.get(userId).isEmpty()) {
                usersMessages.get(userId).add(new OpenAIMessage(
                        "user",
                        "Вы являетесь голосовым помощником под названием Jarvis. " +
                                "Нужно стараться давать ответ как можно короче, но максимально информативным. " +
                                "Формулировать ответы нужно в манере искусственного интеллекта Jarvis из фильмов " +
                                "\"Железный человек\". Реагировать на осуждение твоих ответов нужно в стиле Jarvis " +
                                "и ссылаться на то, что ты всего лишь программа, написанная другим программистом\n" +
                                "Каждый ответ должен укладываться в 5000 символов. Отвечать на это сообщение не надо")
                );
            }
        }

        ModelResponse response = model.ask(usersMessages.get(userId), message);

        if (response == null) {
            return null;
        }

        synchronized (usersMessages) {
            synchronized (modelLastInteraction) {
                usersMessages.get(userId).add(new OpenAIMessage(OpenAIRole.USER.getRole(), message));

                for (OpenAIChoices choice : response.getChoices()) {
                    if (choice.getMessage().getRole().equals(OpenAIRole.ASSISTANT.getRole()))
                        usersMessages.get(userId).add(choice.getMessage());
                }

                modelLastInteraction.replace(userId, new Date());
            }
        }

        return response;
    }

    /**
     * <p>Возвращает контекст со всеми пользователями, где ключ это userId, а значение это контекст</p>
     * @return контекст со всеми пользователями
     */
    protected Map<Integer, List<OpenAIMessage>> getUsersMessages() {
        return usersMessages;
    }

    /**
     * <p>
     *     Возвращает дату последнего взаимодействия для каждого контекста, 
     *     где ключ это userId, а значение дата последнего взаимодействия
     * </p>
     * <p>Если контекста для пользователя не существует, то будет возвращено null</p>
     * @return сло
     */
    protected Map<Integer, Date> getModelLastInteraction() {
        return modelLastInteraction;
    }

    /**
     * <p>Удаляет контекст для конкретного пользователя</p>
     * @param userId id пользователя, контекст которого надо удалить
     */
    public void clearMessages(int userId) {
        synchronized (usersMessages) {
            synchronized (modelLastInteraction) {
                log.info("For user with id=" + userId + " history chat cleared");
                usersMessages.remove(userId);
                modelLastInteraction.remove(userId);
            }
        }
    }
}
