package com.iffomko.voiceAssistant.APIs.speech;

import com.iffomko.voiceAssistant.APIs.speech.data.RecognitionResponse;

/**
 * <p>Клиент, который позволяет обращаться к Yandex API за распознаванием и синтезированием голоса</p>
 */
public class YandexClient {
    private final YandexRecognition recognition;
    private final YandexSynthesis synthesis;

    /**
     * <p>Создает класс YandexClient и заполняет его поля нужными значениями</p>
     * @param apiKey ключ от Yandex API
     */
    public YandexClient(String apiKey) {
        this.recognition = new YandexRecognition(apiKey);
        this.synthesis = new YandexSynthesis(apiKey);
    }

    /**
     * <p>
     *     Возвращает объект, который занимается распознаванием голоса. С помощью этого метода можно только настроить
     *     этот объект по свои нужды.
     * </p>
     * @return объект для распознавания голоса
     */
    public YandexRecognition getRecognition() {
        return recognition;
    }

    /**
     * <p>
     *     Возвращает объект, который занимается синтезированием голоса. С помощью этого метода можно только настроить
     *     этот объект по свои нужды.
     * </p>
     * @return объект для синтезирования голоса
     */
    public YandexSynthesis getSynthesis() {
        return synthesis;
    }

    /**
     * <p>Метод, который позволяет распознать речь в входящем аудио</p>
     * @param voice входящее аудио в виде массива байтов
     * @return объект <code>RecognitionResponse</code>, в котором содержится в поле <code>result</code> распознанный текст
     */
    public RecognitionResponse recognise(byte[] voice) {
        return recognition.recognition(voice);
    }

    /**
     * <p>Метод, который позволяет синтезировать речь</p>
     * @param text текст, который надо озвучить
     * @return массив байтов синтезированной речи
     */
    public byte[] synthesis(String text) {
        return synthesis.synthesize(text);
    }
}
