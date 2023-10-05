package com.zand.system.transactionrestservice.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.zand.system.common.messaging.kafka.message.FundTransferRQMessage;
import com.zand.system.transactionrestservice.builder.FundTransferMessageBuilder;
import com.zand.system.transactionrestservice.builder.TransactionDetailEntityBuilder;
import com.zand.system.transactionrestservice.dto.Currency;
import com.zand.system.transactionrestservice.dto.FundTransferRQ;
import com.zand.system.transactionrestservice.dto.FundTransferRS;
import com.zand.system.transactionrestservice.entity.AccountDetail;
import com.zand.system.transactionrestservice.entity.TransactionDetails;
import com.zand.system.transactionrestservice.exception.InvalidRequestException;
import com.zand.system.transactionrestservice.messaging.producer.FundTxnPublisher;
import com.zand.system.transactionrestservice.repository.AccountDetailRepository;
import com.zand.system.transactionrestservice.repository.TransactionDetailsRepository;
import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class TransactionServiceTest {

//    @Test
//    public void testDoFundTransferTransaction() {
//        // Create mock objects
//        FundTxnPublisher fundTxnPublisher = mock(FundTxnPublisher.class);
//        AccountDetailRepository accountDetailRepository = mock(AccountDetailRepository.class);
//        TransactionDetailsRepository transactionDetailsRepository = mock(TransactionDetailsRepository.class);
//
//        // Create a new TransactionService object
//        TransactionService transactionService = new TransactionService(fundTxnPublisher, accountDetailRepository, transactionDetailsRepository);
//
//        // Create mock data
//        FundTransferRQ fundTransferRQ = new FundTransferRQ();
//        fundTransferRQ.setFromAccountId("123456");
//        fundTransferRQ.setToAccountId("789012");
//        fundTransferRQ.setCurrency(Currency.AED);
//        fundTransferRQ.setAmount(BigDecimal.valueOf(100.0));
//
//        AccountDetail fromAccountDetails = new AccountDetail();
//        fromAccountDetails.setAccountId("123456");
//        fromAccountDetails.setBalance(BigDecimal.valueOf(1000.0));
//
//        AccountDetail toAccountDetails = new AccountDetail();
//        toAccountDetails.setAccountId("789012");
//        toAccountDetails.setBalance(BigDecimal.valueOf(500.0));
//
//        FundTransferRQMessage fundTransferMessage = FundTransferMessageBuilder.mapToFundTransferMessage(fundTransferRQ);
//
//        TransactionDetails transactionDetails = TransactionDetailEntityBuilder.buildTransactionEntity(fundTransferMessage);
//
//        // Set up the accountDetailRepository.findByAccountId method to return the mock AccountDetail objects
//        when(accountDetailRepository.findByAccountId("123456")).thenReturn(Mono.just(fromAccountDetails));
//        when(accountDetailRepository.save(any())).thenReturn(Mono.just(fromAccountDetails));
//        when(accountDetailRepository.findByAccountId("789012")).thenReturn(Mono.just(toAccountDetails));
//        when(accountDetailRepository.save(any())).thenReturn(Mono.just(toAccountDetails));
//
//        // Set up the transactionDetailsRepository.save method to return the mock TransactionDetails object
//        when(transactionDetailsRepository.save(any())).thenReturn(Mono.just(transactionDetails));
//
//        // Call the doFundTransferTransaction method and verify that the returned Mono emits the expected FundTransferRS object
//        Mono<FundTransferRS> result = transactionService.doFundTransferTransaction(fundTransferRQ);
//        StepVerifier.create(result)
//                .expectNextMatches(fundTransferRS -> {
//                    return fundTransferRS.getStatus() == FundTransferRS.Status.SUCCESS;
//                })
//                .verifyComplete();
//    }

    @Test
    public void testDoFundTransferTransactionInsufficientBalance() {
        // Create mock objects
        FundTxnPublisher fundTxnPublisher = mock(FundTxnPublisher.class);
        AccountDetailRepository accountDetailRepository = mock(AccountDetailRepository.class);
        TransactionDetailsRepository transactionDetailsRepository = mock(TransactionDetailsRepository.class);

        // Create a new TransactionService object
        TransactionService transactionService = new TransactionService(fundTxnPublisher, accountDetailRepository, transactionDetailsRepository);

        // Create mock data
        FundTransferRQ fundTransferRQ = new FundTransferRQ();
        fundTransferRQ.setFromAccountId("123456");
        fundTransferRQ.setToAccountId("789012");
        fundTransferRQ.setAmount(BigDecimal.valueOf(10000.0));

        AccountDetail fromAccountDetails = new AccountDetail();
        fromAccountDetails.setAccountId("123456");
        fromAccountDetails.setBalance(BigDecimal.valueOf(1000.0));

        AccountDetail toAccountDetails = new AccountDetail();
        toAccountDetails.setAccountId("789012");
        toAccountDetails.setBalance(BigDecimal.valueOf(500.0));

        // Set up the accountDetailRepository.findByAccountId method to return the mock AccountDetail objects
        when(accountDetailRepository.findByAccountId("123456")).thenReturn(Mono.just(fromAccountDetails));
        when(accountDetailRepository.findByAccountId("789012")).thenReturn(Mono.just(toAccountDetails));

        // Call the doFundTransferTransaction method and verify that the returned Mono emits an error
        Mono<FundTransferRS> result = transactionService.doFundTransferTransaction(fundTransferRQ);
        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    public void testDoFundTransferTransactionFromAccountNotFound() {
        // Create mock objects
        FundTxnPublisher fundTxnPublisher = mock(FundTxnPublisher.class);
        AccountDetailRepository accountDetailRepository = mock(AccountDetailRepository.class);
        TransactionDetailsRepository transactionDetailsRepository = mock(TransactionDetailsRepository.class);

        // Create a new TransactionService object
        TransactionService transactionService = new TransactionService(fundTxnPublisher, accountDetailRepository, transactionDetailsRepository);

        // Create mock data
        FundTransferRQ fundTransferRQ = new FundTransferRQ();
        fundTransferRQ.setFromAccountId("123456");
        fundTransferRQ.setToAccountId("789012");
        fundTransferRQ.setAmount(BigDecimal.valueOf(100.0));

        AccountDetail toAccountDetails = new AccountDetail();
        toAccountDetails.setAccountId("789012");
        toAccountDetails.setBalance(BigDecimal.valueOf(500.0));

        // Set up the accountDetailRepository.findByAccountId method to return the mock AccountDetail objects
        when(accountDetailRepository.findByAccountId("123456")).thenReturn(Mono.empty());
        when(accountDetailRepository.findByAccountId("789012")).thenReturn(Mono.just(toAccountDetails));

        // Call the doFundTransferTransaction method and verify that the returned Mono emits an error
        Mono<FundTransferRS> result = transactionService.doFundTransferTransaction(fundTransferRQ);
        StepVerifier.create(result)
                .expectError(InvalidRequestException.class)
                .verify();
    }

    @Test
    public void testDoFundTransferTransactionToAccountNotFound() {
        // Create mock objects
        FundTxnPublisher fundTxnPublisher = mock(FundTxnPublisher.class);
        AccountDetailRepository accountDetailRepository = mock(AccountDetailRepository.class);
        TransactionDetailsRepository transactionDetailsRepository = mock(TransactionDetailsRepository.class);

        // Create a new TransactionService object
        TransactionService transactionService = new TransactionService(fundTxnPublisher, accountDetailRepository, transactionDetailsRepository);

        // Create mock data
        FundTransferRQ fundTransferRQ = new FundTransferRQ();
        fundTransferRQ.setFromAccountId("123456");
        fundTransferRQ.setToAccountId("789012");
        fundTransferRQ.setAmount(BigDecimal.valueOf(100.0));

        AccountDetail fromAccountDetails = new AccountDetail();
        fromAccountDetails.setAccountId("123456");
        fromAccountDetails.setBalance(BigDecimal.valueOf(1000.0));

        // Set up the accountDetailRepository.findByAccountId method to return the mock AccountDetail objects
        when(accountDetailRepository.findByAccountId("123456")).thenReturn(Mono.just(fromAccountDetails));
        when(accountDetailRepository.findByAccountId("789012")).thenReturn(Mono.empty());

        // Call the doFundTransferTransaction method and verify that the returned Mono emits an error
        Mono<FundTransferRS> result = transactionService.doFundTransferTransaction(fundTransferRQ);
        StepVerifier.create(result)
                .expectError(InvalidRequestException.class)
                .verify();
    }
}