package org.muratcan.petvaccine.reminder.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.muratcan.petvaccine.reminder.bot.VaccineReminderBot;
import org.muratcan.petvaccine.reminder.config.KafkaConfig;
import org.muratcan.petvaccine.reminder.model.VaccineReminder;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VaccineReminderConsumer {

    private final VaccineReminderBot vaccineReminderBot;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaConfig kafkaConfig;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "vaccine-reminder-topic", groupId = "vaccine-reminder-group")
    public void handleVaccineReminder(String message) {
        try {
            VaccineReminder reminder = objectMapper.readValue(message, VaccineReminder.class);
            vaccineReminderBot.sendMessage(reminder.getChatId(), reminder.getMessage());
        } catch (Exception e) {
            sendToDeadLetterQueue(message);
        }
    }

    private void sendToDeadLetterQueue(String message) {
        kafkaTemplate.send(kafkaConfig.getVaccineReminderDeadLetterTopic(), message);
    }
}
