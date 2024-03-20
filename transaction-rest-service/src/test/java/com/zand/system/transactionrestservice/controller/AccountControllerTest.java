package com.zand.system.transactionrestservice.controller;

import com.zand.system.transactionrestservice.dto.AccountBalanceRS;
import com.zand.system.transactionrestservice.service.AccountService;
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

@WebFluxTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AccountService accountService;

    @MockBean
    WebProperties.Resources resources;

    @Test
    public void testGetAccountBalance() {
        // Set up mock data
        String accountId = "123456";
        AccountBalanceRS accountBalanceRS = new AccountBalanceRS();
        accountBalanceRS.setAccountId(accountId);
        accountBalanceRS.setBalance(new BigDecimal(1000.0));

        // Set up mock service response
        Mockito.when(accountService.getAccountBalance(accountId)).thenReturn(Mono.just(accountBalanceRS));

        // Call the endpoint and verify the response
        webTestClient.get()
                .uri("/v1/accounts/{accountId}/balance", accountId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AccountBalanceRS.class);
    }
}
