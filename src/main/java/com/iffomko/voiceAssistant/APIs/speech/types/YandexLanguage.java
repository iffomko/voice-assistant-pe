package com.iffomko.voiceAssistant.APIs.speech.types;

/**
 * <p>Перечисление всех возможных языков доступных для распознавания для запроса</p>
 */
public enum YandexLanguage {
    /**
     * <p>Русский язык</p>
     */
    RUSSIAN("ru-RU"),
    /**
     * <p>Английский язык</p>
     */
    ENGLISH("en-US"),
    /**
     * <p>Немецкий язык</p>
     */
    GERMANY("de-DE"),
    /**
     * <p>Испанский язык</p>
     */
    SPANISH("es-ES"),
    /**
     * <p>Французский язык</p>
     */
    FRENCH("fr-FR"),
    /**
     * <p>Казахский язык</p>
     */
    KAZAKH("kk-KK"),
    /**
     * <p>Тюркский язык</p>
     */
    TURKISH("tr-TR"),
    /**
     * <p>Польский язык</p>
     */
    POLISH("pl-PL"),
    /**
     * <p>Итальянский язык</p>
     */
    ITALIAN("it-IT");

    private final String lang;

    /**
     * <p>Устанавливает язык для распозновательной модели</p>
     * @param lang - язык
     */
    YandexLanguage(String lang) {
        this.lang = lang;
    }

    /**
     * <p>Возвращает язык для распозновательной модели</p>
     * @return - язык
     */
    public String getLang() {
        return lang;
    }
}
