package com.iffomko.voiceAssistant.APIs.speech;

import com.iffomko.voiceAssistant.APIs.speech.data.RecognitionResponse;
import com.iffomko.voiceAssistant.APIs.speech.types.YandexLanguage;
import com.iffomko.voiceAssistant.APIs.speech.types.YandexTopic;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * <p>Класс, который умеет делать запросы к Yandex API и с её помощью синтезировать речь</p>
 */
@Slf4j
public class YandexRecognition {
    private final String apiKey;
    private YandexLanguage lang = null;
    private YandexTopic topic = null;
    private boolean profanityFilter = false;
    private String format = "oggopus";
    public final static String URL = "https://stt.api.cloud.yandex.net/speech/v1/stt:recognize";

    /**
     * <p>Конструктор, который принимает ключ от Yandex API, чтобы можно авторизоваться с его помощью в системе</p>
     * @param apiKey ключ от Yandex API для авторизации
     */
    public YandexRecognition(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * <p>Формирует строчку для Query параметров для запроса</p>
     * @return строка содержащая query параметры
     */
    private String getQueryParams() {
        StringBuilder queries = new StringBuilder();

        if (lang != null) {
            queries.append("lang=");
            queries.append(lang.getLang());
        }

        if (topic != null) {
            if (!queries.isEmpty()) {
                queries.append("&");
            }

            queries.append("topic=");
            queries.append(topic.getTopic());
        }

        if (!queries.isEmpty()) {
            queries.append("&");
        }

        queries.append("profanityFilter=");
        queries.append(profanityFilter);

        if (!queries.isEmpty()) {
            queries.append("&");
        }

        queries.append("format=");
        queries.append(format);

        return queries.toString();
    }

    /**
     * <p>Делает запрос к Yandex API за распознаванием речи по входному аудио</p>
     * @param voice массив байтов аудио, которое нужно распознать
     * @return объект <code>RecognitionResponse</code>, содержащий единственное поле <code>result</code> с распознанным текстом
     */
    protected RecognitionResponse recognition(byte[] voice) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Api-Key " + apiKey);

        HttpEntity<byte[]> request = new HttpEntity<>(voice, headers);

        ResponseEntity<RecognitionResponse> response = null;

        try {
            response =  restTemplate.exchange(
                    URL + "?" + getQueryParams(),
                    HttpMethod.POST,
                    request,
                    RecognitionResponse.class
            );
        } catch (RestClientException e) {
            log.error(e.getMessage());
        }

        if (response == null || response.getBody() == null) {
            log.error("Failed to recognize speech from Yandex API");
            return null;
        }

        log.info("The Yandex API text recognition request was executed successfully");

        return response.getBody();
    }

    /**
     * <p>Возвращает язык, который нужно распознавать, в настройках этого класса</p>
     * @return объект <code>YandexLanguage</code>, который содержит распознаваемый язык
     */
    public YandexLanguage getLang() {
        return lang;
    }

    /**
     * <p>Устанавливает распознаваемый язык для текущих настроек класса</p>
     * @param lang объект <code>YandexLanguage</code>, содержащий распознаваемый язык
     */
    public void setLang(YandexLanguage lang) {
        this.lang = lang;
    }

    /**
     * <p>Возвращает название модели распознавания в настройках этого класса</p>
     * @return объект <code>YandexTopic</code>, содержащий название модели распознавания
     */
    public YandexTopic getTopic() {
        return topic;
    }

    /**
     * <p>Устанавливает модель распознавания для настроек этого класса</p>
     * @param topic объект <code>YandexTopic</code>, содержащий название модели распознавания
     */
    public void setTopic(YandexTopic topic) {
        this.topic = topic;
    }

    /**
     * <p>
     *     Возвращает значение поля profanityFilter.
     *     Если установлен <code>false</code>, то ненормативная лексика не будет исключена из распознавания.
     *     Если установлен <code>true</code>, то нормативная лекция будет исключена из распознавания.
     * </p>
     * @return возвращает значение поля profanityFilter
     */
    public boolean isProfanityFilter() {
        return profanityFilter;
    }

    /**
     * <p>
     *     Устанавливает значение <code>true</code> или <code>false</code> для поля profanityFilter.
     *     Если установлен <code>false</code>, то ненормативная лексика не будет исключена из распознавания.
     *     Если установлен <code>true</code>, то нормативная лекция будет исключена из распознавания.
     * </p>
     * @param profanityFilter <code>true</code> или <code>false</code> - значение, которое нужно установить полю
     */
    public void setProfanityFilter(@NonNull boolean profanityFilter) {
        this.profanityFilter = profanityFilter;
    }

    /**
     * <p>Возвращает формат входного аудио (mp3, oggopus, lpcm)</p>
     * @return возвращает формат входящего аудио
     */
    public String getFormat() {
        return format;
    }

    /**
     * <p>Устанавливает формат входного аудио в настройках этого класса</p>
     * @param format формат входящего аудио (mp3, oggopus, lpcm)
     */
    public void setFormat(@NonNull String format) {
        this.format = format;
    }
}
