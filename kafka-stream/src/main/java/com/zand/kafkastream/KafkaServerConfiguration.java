package com.zand.kafkastream;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.test.EmbeddedKafkaBroker;

import java.util.HashMap;
import java.util.Map;

@Configuration
class KafkaServerConfiguration {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    EmbeddedKafkaBroker broker() {
        return new EmbeddedKafkaBroker(1)
                .kafkaPorts(10091)
                .brokerListProperty("spring.kafka.bootstrap-servers");
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule())
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

//    @Bean
//    public NewTopic topicFundTransferEvent() {
//        return createNewTopicWithDefaults("initiate-fund-transfer-event");
//    }
//
//    @Bean
//    public NewTopic topicFundTransferFailedEvent() {
//        return createNewTopicWithDefaults("fund-transfer-failure-event");
//    }
//
//    private NewTopic createNewTopicWithDefaults(String name) {
//        return TopicBuilder.name(name)
//                .partitions(3)
//                .replicas(1)
//                .build();
//    }
//
//    @KafkaListener(id = "kafka-server-1", topics = "initiate-fund-transfer-event")
//    public void listenFundTransfer(String in) {
//        System.out.println(in);
//    }
//
//    @KafkaListener(id = "kafka-server-2", topics = "fund-transfer-failure")
//    public void listenFundTransferFailed(String in) {
//        System.out.println(in);
//    }

}
