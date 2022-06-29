package com.health.application.medhealth.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public NewTopic sendMailTopic(){
        return TopicBuilder.name("topic-send-email").build();
    }
}
