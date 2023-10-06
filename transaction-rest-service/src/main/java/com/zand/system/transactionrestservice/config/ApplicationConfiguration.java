package com.zand.system.transactionrestservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public WebProperties.Resources resources(){
        return new WebProperties.Resources();
    }

    @Bean
    public NewTopic topicFundTransferFailure(@Value("${kafka.consumer.topic.fund-transfer-failure}") String topicName) {
        return new NewTopic(topicName, 1, (short) 1);
    }

    @Bean
    public NewTopic topicFundTransferSuccess(@Value("${kafka.consumer.topic.fund-transfer-success}") String topicName) {
        return new NewTopic(topicName, 1, (short) 1);
    }

}
