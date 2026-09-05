package com.example.service.impl;

import com.example.controller.TelegramBot;
import com.example.service.UpdateProducer;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class UpdateProducerImpl implements UpdateProducer {
    private static final Logger log = Logger.getLogger(TelegramBot.class);
    @Override
    public void produce(String rabbitQueue, Update update) {
        log.debug(update.getMessage().getText());
    }
}