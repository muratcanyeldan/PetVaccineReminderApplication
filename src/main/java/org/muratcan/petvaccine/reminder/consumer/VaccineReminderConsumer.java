package org.muratcan.petvaccine.reminder.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.muratcan.petvaccine.reminder.bot.VaccineReminderBot;
import org.muratcan.petvaccine.reminder.config.KafkaConfig;
import org.muratcan.petvaccine.reminder.model.VaccineReminder;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VaccineReminderConsumer {

    private final VaccineReminderBot vaccineReminderBot;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaConfig kafkaConfig;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${kafka.topic.reminder-topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleVaccineReminder(@Payload String message) {
        try {
            VaccineReminder reminder = objectMapper.readValue(message, VaccineReminder.class);
            vaccineReminderBot.sendMessage(reminder.getChatId(), reminder.getMessage());
        } catch (Exception e) {
            sendToDeadLetterQueue(String.format("Cause : %s, failed message : %s", e.getMessage(), message));
        }
    }

    private void sendToDeadLetterQueue(String message) {
        kafkaTemplate.send(kafkaConfig.getVaccineReminderDeadLetterTopic(), message);
    }
}
