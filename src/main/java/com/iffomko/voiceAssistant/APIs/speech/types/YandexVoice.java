package com.iffomko.voiceAssistant.APIs.speech.types;

/**
 * <p>Перечисление всех голосов, которые доступны для синтеза голоса</p>
 */
public enum YandexVoice {
    FILIPP("filipp", YandexLanguage.RUSSIAN),
    ALENA("alena", YandexLanguage.RUSSIAN),
    ERMIL("ermil", YandexLanguage.RUSSIAN),
    JANE("jane", YandexLanguage.RUSSIAN),
    MADIRUS("madirus", YandexLanguage.RUSSIAN),
    OMAZH("omazh", YandexLanguage.RUSSIAN),
    ZAHAR("zahar", YandexLanguage.RUSSIAN),
    LEA("lea", YandexLanguage.GERMANY),
    JOHN("john", YandexLanguage.ENGLISH),
    AMIRA("amira", YandexLanguage.KAZAKH),
    MADI("madi", YandexLanguage.RUSSIAN);

    private final String voice;
    private final YandexLanguage lang;

    YandexVoice(String voice, YandexLanguage lang) {
        this.lang = lang;
        this.voice = voice;
    }

    /**
     * <p>Возвращает язык для синтеза речи</p>
     * @return объект <code>YandexLanguage</code>, содержащий язык для синтеза речи
     */
    public YandexLanguage getLang() {
        return lang;
    }

    /**
     * <p>Возвращает название голоса для синтеза речи</p>
     * @return название голоса
     */
    public String getVoice() {
        return voice;
    }
}
