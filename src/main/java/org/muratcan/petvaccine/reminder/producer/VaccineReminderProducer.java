package org.muratcan.petvaccine.reminder.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.muratcan.petvaccine.reminder.config.KafkaConfig;
import org.muratcan.petvaccine.reminder.model.VaccineReminder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VaccineReminderProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper;

    private final KafkaConfig kafkaConfig;

    public void sendVaccineReminder(VaccineReminder reminder) throws JsonProcessingException {
        String message = objectMapper.writeValueAsString(reminder);
        kafkaTemplate.send(kafkaConfig.getVaccineReminderTopic(), message);
    }
}
