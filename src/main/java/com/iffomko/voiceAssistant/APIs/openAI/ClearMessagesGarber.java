package com.iffomko.voiceAssistant.APIs.openAI;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 *     Этот класс отвечает за очистку сообщений в объектах AIService.
 *     Он очищает сообщения если срок последнего взаимодействия в этом чате на текущий момент больше 1 часа.
 * </p>
 * <p>
 *     Этот класс реализуется интерфейс <code>Runnable</code>, следовательно его нужно запускать в отдельном потоке,
 *     потому что иначе программа просто зависнет.
 * </p>
 */
@Slf4j
public class ClearMessagesGarber implements Runnable {
    private final AIService service;
    private final int timeout;

    private final static long TIME_LIVE = 3600000;


    /**
     * <p>
     *     Конструктор класса <code>ClearMessagesGarber</code>. Он устанавливает время через которое этот класс будет
     *     производить очистку, а также сам сервис, в котором будет производится очистка.
     * </p>
     * @param service сервис, в котором будет производиться очистка
     * @param timeout время через которое будет производиться очистка
     */
    public ClearMessagesGarber(@NonNull AIService service, @NonNull int timeout) {
        this.service = service;
        this.timeout = timeout;
    }

    /**
     * <p>
         * Когда объект реализуется интерфейс {@code Runnable} этот метод используется
         * для создания потока, при запуске потока {@code run} метод этого объекта будет вызван
         * в отдельно исполняющем потоке
     * </p>
     */
    @Override
    public void run() {
        while (true) {
            synchronized (service.getUsersMessages()) {
                synchronized (service.getModelLastInteraction()) {
                    for (Map.Entry<Integer, Date> createdDate : service.getModelLastInteraction().entrySet()) {
                        Date currentDate = new Date();

                        if (Math.abs(currentDate.getTime() - createdDate.getValue().getTime()) > TIME_LIVE) {
                            service.clearMessages(createdDate.getKey());
                        }
                    }
                }
            }

            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        }
    }
}
