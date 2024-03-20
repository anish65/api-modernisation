package com.zand.system.transactionprocessor;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(scanBasePackages = {"com.zand.system.transactionprocessor", "com.zand.system.common.*"})
public class TransactionProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionProcessorApplication.class, args);
	}

	@Bean
	RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	@Bean
	public NewTopic topic(@Value("${kafka.consumer.topic.initiate-fund-transfer}") String topicName) {
		return new NewTopic(topicName, 2, (short) 1);
	}


}
