package com.zand.system.transactionrestservice.controller;

import com.zand.system.transactionrestservice.dto.FundTransferRQ;
import com.zand.system.transactionrestservice.dto.FundTransferRS;
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

@WebFluxTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    WebProperties.Resources resources;

    @Test
    public void testDoFundTransfer() {
        // Set up mock data
        FundTransferRS fundTransferRS = new FundTransferRS();
        FundTransferRQ fundTransferRQ = new FundTransferRQ();

        // Set up mock service response
        Mockito.when(transactionService.doFundTransferTransaction(fundTransferRQ)).thenReturn(Mono.just(fundTransferRS));

        // Call the endpoint and verify the response
        webTestClient.post()
                .uri("/v1/transactions/fundTransfer")
                .bodyValue(fundTransferRQ)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(FundTransferRS.class);
    }
}
