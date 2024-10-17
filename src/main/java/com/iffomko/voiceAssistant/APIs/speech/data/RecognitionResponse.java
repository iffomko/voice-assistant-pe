package com.iffomko.voiceAssistant.APIs.speech.data;

import lombok.Data;

/**
 * <p>Объект ответа, в котором содержится одно единственное поле result, которое содержит распознанный текст</p>
 */
@Data
public class RecognitionResponse {
    private String result;
}
