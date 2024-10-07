package org.muratcan.petvaccine.reminder.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class KafkaConfig {

    @Value("${kafka.topic.reminder-topic}")
    private String vaccineReminderTopic;

    @Value("${kafka.topic.reminder-deadletter-topic}")
    private String vaccineReminderDeadLetterTopic;

}
