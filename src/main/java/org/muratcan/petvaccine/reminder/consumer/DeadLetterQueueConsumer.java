package org.muratcan.petvaccine.reminder.consumer;

import lombok.RequiredArgsConstructor;
import org.muratcan.petvaccine.reminder.model.VaccineReminder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeadLetterQueueConsumer {

    private static final Logger logger = LoggerFactory.getLogger(DeadLetterQueueConsumer.class);

    @KafkaListener(topics = "vaccine-dead-letter-topic", groupId = "vaccine-dlq-group")
    public void handleDeadLetter(VaccineReminder reminder) {
        logger.error("Dead-letter message: {}", reminder);
    }
}

