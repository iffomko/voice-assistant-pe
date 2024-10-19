package com.iffomko.voiceAssistant.controllers;

import com.iffomko.voiceAssistant.APIs.speech.types.YandexFormat;
import com.iffomko.voiceAssistant.controllers.data.AnswerRequestDTO;
import com.iffomko.voiceAssistant.controllers.data.AnswerResponseDTO;
import com.iffomko.voiceAssistant.controllers.errors.AnswerError;
import com.iffomko.voiceAssistant.controllers.services.AnswerService;
import com.iffomko.voiceAssistant.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/answer")
public class AnswerController {
    private final AnswerService answerService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AnswerController(
            @Qualifier("answerService") AnswerService answerService,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.answerService = answerService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * <p>Контроллер, который отвечает за ответ на вопрос, который содержится в аудио</p>
     */
    @PostMapping(produces="application/json")
    @PreAuthorize("hasAuthority('get:answer')")
    public ResponseEntity<AnswerResponseDTO> getAnswer(
            @RequestBody AnswerRequestDTO body,
            HttpServletRequest httpServletRequest
    ) {
        if (
                body.getFormat() != null &&
                !body.getFormat().equals(YandexFormat.OGGOPUS.getFormat()) &&
                !body.getFormat().equals(YandexFormat.MP3.getFormat()) &&
                !body.getFormat().equals(YandexFormat.LPCM.getFormat())
        ) {
            AnswerError error = new AnswerError();
            error.setCode(HttpStatus.BAD_REQUEST.value());
            error.setMessage("Вы ввели некорректный формат аудио: " + body.getFormat());

            return ResponseEntity.badRequest().body(new AnswerResponseDTO(error));
        }

        if (body.getFormat() == null) {
            body.setFormat(YandexFormat.OGGOPUS.getFormat());
        }

        if (body.getProfanityFilter() == null) {
            body.setProfanityFilter(false);
        }

        String response = answerService.getAnswer(
                jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(httpServletRequest)),
                body.getAudio(),
                body.getFormat(),
                body.getProfanityFilter()
        );

        if (response == null) {
            AnswerError error = new AnswerError();
            error.setCode(HttpStatus.BAD_REQUEST.value());
            error.setMessage("Не удалось получить ответ");

            return ResponseEntity.badRequest().body(new AnswerResponseDTO(error));
        }

        return ResponseEntity.ok(new AnswerResponseDTO(response, "base64", body.getFormat()));
    }
}
