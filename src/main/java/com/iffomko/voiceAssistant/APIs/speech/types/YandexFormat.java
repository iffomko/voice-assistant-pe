package com.iffomko.voiceAssistant.APIs.speech.types;

/**
 * <p>Перечисление, которое содержит доступные форматы для аудио</p>
 */
public enum YandexFormat {
    MP3("mp3"),
    OGGOPUS("oggopus"),
    LPCM("lpcm");

    private final String format;

    YandexFormat(String format) {
        this.format = format;
    }

    /**
     * <p>Возвращает формат для аудио</p>
     * @return формат
     */
    public String getFormat() {
        return format;
    }
}
