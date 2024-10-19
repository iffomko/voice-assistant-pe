package com.iffomko.voiceAssistant.controllers.errors;

import lombok.Data;

/**
 * <p>Объект ошибки. Он сериализуется в JSON, а так же содержит в себе сообщение ошибки и код ошибки</p>
 */
@Data
public class AnswerError {
    private String message;
    private int code;
}
