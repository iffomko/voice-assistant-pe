package com.iffomko.voiceAssistant.controllers.data;

import com.iffomko.voiceAssistant.controllers.errors.AnswerError;
import lombok.Data;

@Data
public class AnswerResponseDTO {
    private String voice = null;
    private String encode = null;
    private String format = null;
    private AnswerError error;

    public AnswerResponseDTO() {}

    public AnswerResponseDTO(String byteAudio, String encode, String format) {
        this.voice = byteAudio;
        this.encode = encode;
        this.format = format;
    }

    public AnswerResponseDTO(AnswerError error) {
        this.error = error;
    }
}
