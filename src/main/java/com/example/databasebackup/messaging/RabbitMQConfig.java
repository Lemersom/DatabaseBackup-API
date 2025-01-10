package com.example.databasebackup.messaging;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${queue.backup.name}")
    private String backupQueueName;

    @Bean
    public Queue backupQueue() {
        return new Queue(backupQueueName, true);
    }

    @Bean
    public MessageConverter jsonMassageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();

        typeMapper.addTrustedPackages("com.example.databasebackup.model");
        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }

}
