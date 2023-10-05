package com.zand.system.transactionrestservice.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.zand.system.transactionrestservice.dto.AccountBalanceRS;
import com.zand.system.transactionrestservice.dto.Currency;
import com.zand.system.transactionrestservice.entity.AccountDetail;
import com.zand.system.transactionrestservice.repository.AccountDetailRepository;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

public class AccountServiceTest {

    @Test
    public void testGetAccountBalance() {
        // Create a mock AccountDetailRepository object
        AccountDetailRepository accountDetailRepository = mock(AccountDetailRepository.class);

        // Create a new AccountService object
        AccountService accountService = new AccountService(accountDetailRepository);

        // Create a mock AccountDetail object
        AccountDetail accountDetail = new AccountDetail();
        accountDetail.setCifId("123456");
        accountDetail.setAccountId("789012");
        accountDetail.setBalance(new BigDecimal(1000.0));
        accountDetail.setCurrency("USD");

        // Set up the accountDetailRepository.findByAccountId method to return the mock AccountDetail object
        when(accountDetailRepository.findByAccountId("789012")).thenReturn(Mono.just(accountDetail));

        // Call the getAccountBalance method and verify that the returned Mono emits the expected AccountBalanceRS object
        Mono<AccountBalanceRS> result = accountService.getAccountBalance("789012");
        StepVerifier.create(result)
                .expectNextMatches(accountBalanceRS -> {
                    return accountBalanceRS.getCifId().equals("123456")
                            && accountBalanceRS.getAccountId().equals("789012")
                            && new BigDecimal(1000).compareTo(accountBalanceRS.getBalance())==0
                            && Currency.USD.equals(accountBalanceRS.getCurrency());
                })
                .verifyComplete();
    }

    @Test
    public void testGetAccountBalanceNotFound() {
        // Create a mock AccountDetailRepository object
        AccountDetailRepository accountDetailRepository = mock(AccountDetailRepository.class);

        // Create a new AccountService object
        AccountService accountService = new AccountService(accountDetailRepository);

        // Set up the accountDetailRepository.findByAccountId method to return an empty Mono
        when(accountDetailRepository.findByAccountId("789012")).thenReturn(Mono.empty());

        // Call the getAccountBalance method and verify that the returned Mono emits an error
        Mono<AccountBalanceRS> result = accountService.getAccountBalance("789012");
        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();
    }
}
