package com.zand.system.transactionrestservice.controller;

import com.zand.system.transactionrestservice.dto.Currency;
import com.zand.system.transactionrestservice.dto.TransactionRQ;
import com.zand.system.transactionrestservice.dto.TransactionRS;
import com.zand.system.transactionrestservice.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@WebFluxTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    WebProperties.Resources resources;

    @Test
    public void testDoDebitTransaction() {
        // Set up mock data
        TransactionRS transactionRS = new TransactionRS();
        TransactionRQ transactionRQ = new TransactionRQ();
        transactionRQ.setCurrency(Currency.AED);
        transactionRQ.setAmount(new BigDecimal(1));
        transactionRQ.setDescription("testing");
        transactionRQ.setAccountId("123456");

        // Set up mock service response
        Mockito.when(transactionService.doDebitTransaction(transactionRQ)).thenReturn(Mono.just(transactionRS));

        // Call the endpoint and verify the response
        webTestClient.post()
                .uri("/v1/transactions/debit")
                .bodyValue(transactionRQ)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TransactionRS.class);
    }

    @Test
    public void testDoCreditTransaction() {
        // Set up mock data
        TransactionRS transactionRS = new TransactionRS();
        TransactionRQ transactionRQ = new TransactionRQ();
        transactionRQ.setCurrency(Currency.AED);
        transactionRQ.setAmount(new BigDecimal(1));
        transactionRQ.setDescription("testing");
        transactionRQ.setAccountId("123456");

        // Set up mock service response
        Mockito.when(transactionService.doDebitTransaction(transactionRQ)).thenReturn(Mono.just(transactionRS));

        // Call the endpoint and verify the response
        webTestClient.post()
                .uri("/v1/transactions/credit")
                .bodyValue(transactionRQ)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TransactionRS.class);
    }
}
