package com.zand.system.transactionrestservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.zand.system.transactionrestservice", "com.zand.system.common"})
public class TransactionRestServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionRestServiceApplication.class, args);
	}

}
