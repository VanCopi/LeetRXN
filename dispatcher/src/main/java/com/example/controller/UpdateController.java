package com.example.controller;


import com.example.utils.MessageUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class UpdateController {
    private TelegramBot telegramBot;
    private MessageUtils messageUtils;

    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }
    private static final Logger log = Logger.getLogger(TelegramBot.class);

    public void processUpdate(Update update) {
        if (update == null) {
            log.error("Received update is null");
            return;
        }

        if (update != null) {
            distributionMessageByType(update);
        } else {
            log.error("Received unsupported message type: " + update);
        }
    }

    private void distributionMessageByType(Update update) {
        var message = update.getMessage();
        if (message.getText() != null) {
            processTextMessage(update);
        } else if (message.getDocument() != null) {
            processDocMessage(update);
        } else if (message.getPhoto() != null) {
            processPhotoMessage(update);
        } else {
            unsupportedMessage(update);
        }
    }

    private void unsupportedMessage(Update update) {
        var sendMessage = messageUtils
    }

    private void processPhotoMessage(Update update) {

    }

    private void processDocMessage(Update update) {

    }

    private void processTextMessage(Update update) {

    }
}