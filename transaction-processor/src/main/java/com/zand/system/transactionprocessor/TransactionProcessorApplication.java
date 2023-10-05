package com.zand.system.transactionprocessor;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = {"com.zand.system.transactionprocessor", "com.zand.system.common.*"})
public class TransactionProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionProcessorApplication.class, args);
	}

	@Bean
	public NewTopic topic(@Value("${kafka.consumer.group-id.initiate-fund-transfer}") String topicName) {
		return new NewTopic("initiate-fund-transfer", 3, (short) 1);
	}
}
