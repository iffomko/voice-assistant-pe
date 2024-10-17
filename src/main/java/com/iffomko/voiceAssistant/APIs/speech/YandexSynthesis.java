package com.iffomko.voiceAssistant.APIs.speech;

import com.iffomko.voiceAssistant.APIs.speech.types.YandexVoice;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * <p>Класс, который умеет делать запросы к Yandex API за синтезом речи</p>
 */
@Slf4j
public class YandexSynthesis {
    private final String apiKey;
    private YandexVoice voice = YandexVoice.FILIPP;
    private String format = "oggopus";

    public static final String URL = "https://tts.api.cloud.yandex.net/speech/v1/tts:synthesize\n";

    /**
     * <p>Конструктор, который принимает ключ от Yandex API, чтобы можно авторизоваться с его помощью в системе</p>
     * @param apiKey ключ от Yandex API для авторизации
     */
    public YandexSynthesis(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * <p>Делает запрос на Yandex API, чтобы синтезировать речь по тексту</p>
     * @param text текст, который необходимо озвучить
     * @return массив байтов озвученного текста
     */
    protected byte[] synthesize(@NonNull String text) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", "Api-Key " + apiKey);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("text", text);
        body.add("voice", voice.getVoice());
        body.add("lang", voice.getLang().getLang());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<byte[]> response = null;

        try {
            response =  restTemplate.exchange(URL, HttpMethod.POST, request, byte[].class);
        } catch (RestClientException e) {
            log.error(e.getMessage());
        }

        if (response == null || response.getBody() == null) {
            log.error("Failed to synthesize speech by text from Yandex API");
            return null;
        }

        log.info("Successfully synthesized speech using Yandex API");

        return response.getBody();
    }

    /**
     * <p>Возвращает голос для синтеза речи, установленный в настройках этого класса</p>
     * @return объект <code>YandexVoice</code>, в котором хранится текущий голос в настройках
     */
    public YandexVoice getVoice() {
        return voice;
    }

    /**
     * <p>Устанавливает голос для синтеза речи</p>
     * @param voice голос для синтеза речи, который нужно установить
     */
    public void setVoice(@NonNull YandexVoice voice) {
        this.voice = voice;
    }

    /**
     * <p>Возвращает формат выходной аудиодорожки (mp3, oggopus, lpcm)</p>
     * @return формат аудиодорожки
     */
    public String getFormat() {
        return format;
    }

    /**
     * <p>Устанавливает формат для выходной аудиодорожки (mp3, oggopus, lpcm)</p>
     * @param format формат аудиодорожки
     */
    public void setFormat(@NonNull String format) {
        this.format = format;
    }
}
