package com.iffomko.voiceAssistant.APIs.speech.types;

/**
 * <p>Перечисление всех версий моделей распознавания доступных для запроса</p>
 */
public enum YandexTopic {
    /**
     * <p>Основная версия модели</p>
     */
    GENERAL("general"),
    /**
     * <p>Версия-кандидат для релиза, которую вы можете тестировать</p>
     */
    GENERAL_RC("general:rc"),
    /**
     * <p>Предыдущая версия модели</p>
     */
    GENERAL_DEPRECATED("general:deprecated");

    private final String topic;

    /**
     * <p>Устанавливает текущее значение распозновательной модели</p>
     * @param topic - версия модели
     */
    YandexTopic(String topic) {
        this.topic = topic;
    }

    /**
     * <p>Возвращает тег распазновательной модели</p>
     * @return - тег модели
     */
    public String getTopic() {
        return topic;
    }
}
