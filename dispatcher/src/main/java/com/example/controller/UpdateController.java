package com.example.controller;

import com.example.service.UpdateProducer;
import com.example.utils.MessageUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import static com.example.model.RabbitQueue.*;

@Component
public class UpdateController {
    private TelegramBot telegramBot;
    private final MessageUtils messageUtils;
    private final UpdateProducer updateProducer;

    public UpdateController(MessageUtils messageUtils, UpdateProducer updateProducer) {
        this.messageUtils = messageUtils;
        this.updateProducer = updateProducer;
    }

    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }
    private static final Logger log = Logger.getLogger(UpdateController.class);

    public void processUpdate(Update update) {
        if (update == null) {
            log.error("Received update is null");
            return;
        }

        if (!update.hasMessage()) {
            log.debug("Update without message (callback, inline, etc.)");
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

        if (message == null) {
            log.warn("Message is null, ignoring update");
            return;
        }

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
        var sendMessage = messageUtils.generateSendMessageWithText(update,
                "Unsupported type of message is received");
        setView(sendMessage);
    }

    private void setView(SendMessage sendMessage) {
        telegramBot.sendAnswerMessage(sendMessage);
    }

    private void processPhotoMessage(Update update) {
        updateProducer.produce(PHOTO_MESSAGE_UPDATE, update);
        setFileReceivedView(update);
    }

    private void setUnsupportedMessageTypeView(Update update) {
        var sendMessage = messageUtils.generateSendMessageWithText(update, "UnsupportedType");
        setView(sendMessage);
    }
    private void setFileReceivedView(Update update) {
        var sendMessage = messageUtils.generateSendMessageWithText(update, "File in processing...");
        setView(sendMessage);
    }

    private void processDocMessage(Update update) {
        updateProducer.produce(DOC_MESSAGE_UPDATE, update);
        setFileReceivedView(update);
    }

    private void processTextMessage(Update update) {
        updateProducer.produce(TEXT_MESSAGE_UPDATE, update);
        setFileReceivedView(update);
    }
}