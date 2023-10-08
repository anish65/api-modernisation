package com.zand.system.transactionrestservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.zand.system.common.messaging.kafka.message.TransactionRQMessage;
import com.zand.system.common.messaging.kafka.message.TransactionRSMessage;
import com.zand.system.transactionrestservice.dto.Currency;
import com.zand.system.transactionrestservice.dto.TransactionRQ;
import com.zand.system.transactionrestservice.dto.TransactionRS;
import com.zand.system.transactionrestservice.entity.AccountDetail;
import com.zand.system.transactionrestservice.entity.TransactionDetails;
import com.zand.system.transactionrestservice.exception.InvalidRequestException;
import com.zand.system.transactionrestservice.messaging.producer.FundTxnPublisher;
import com.zand.system.transactionrestservice.repository.AccountDetailRepository;
import com.zand.system.transactionrestservice.repository.TransactionDetailsRepository;
import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.support.SendResult;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class TransactionServiceTest {

    @Mock
    private FundTxnPublisher fundTxnPublisher;

    @Mock
    private TransactionDetailsRepository transactionDetailsRepository;

    @Mock
    private AccountDetailRepository accountDetailRepository;

    @MockBean
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Create a new TransactionService object
        transactionService = new TransactionService(fundTxnPublisher, accountDetailRepository, transactionDetailsRepository);
    }


    @Test
    public void testDoFundTransferTransactionInsufficientBalance() {
        // Create mock data
        TransactionRQ transactionRQ = new TransactionRQ();
        transactionRQ.setAccountId("123456");
        transactionRQ.setAmount(BigDecimal.valueOf(10000.0));

        AccountDetail accountDetail = new AccountDetail();
        accountDetail.setAccountId("123456");
        accountDetail.setBalance(BigDecimal.valueOf(1000.0));

        // Set up the accountDetailRepository.findByAccountId method to return the mock AccountDetail objects
        when(accountDetailRepository.findByAccountId("123456")).thenReturn(Mono.just(accountDetail));

        // Call the doFundTransferTransaction method and verify that the returned Mono emits an error
        Mono<TransactionRS> result = transactionService.doDebitTransaction(transactionRQ);
        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    public void testDoDebitTransactionAccountNotFound() {
        // Create mock data
        TransactionRQ transactionRQ = new TransactionRQ();
        transactionRQ.setAccountId("123456");
        transactionRQ.setAmount(BigDecimal.valueOf(100.0));

        // Set up the accountDetailRepository.findByAccountId method to return the mock AccountDetail objects
        when(accountDetailRepository.findByAccountId("123456")).thenReturn(Mono.empty());

        // Call the doFundTransferTransaction method and verify that the returned Mono emits an error
        Mono<TransactionRS> result = transactionService.doDebitTransaction(transactionRQ);
        StepVerifier.create(result)
                .expectError(InvalidRequestException.class)
                .verify();
    }

    @Test
    void doDebitTransaction_shouldReturnSuccess_whenAccountFoundAndTransactionSuccessful() {
        String accountId = "12345";
        BigDecimal amount = BigDecimal.valueOf(100.0);
        TransactionRQ transactionRQ = new TransactionRQ();
        transactionRQ.setAccountId(accountId);
        transactionRQ.setAmount(amount);
        transactionRQ.setCurrency(Currency.AED);

        AccountDetail accountDetail = new AccountDetail();
        accountDetail.setAccountId(accountId);
        accountDetail.setBalance(amount);

        when(accountDetailRepository.findByAccountId(accountId)).thenReturn(Mono.just(accountDetail));
        when(accountDetailRepository.save(accountDetail)).thenReturn(Mono.just(accountDetail));

        when(transactionDetailsRepository.save(any())).thenReturn(Mono.just(TransactionDetails.builder().build()));

        SendResult<String, TransactionRQMessage> sendResult = mock(SendResult.class);
        ProducerRecord<String, TransactionRQMessage> producerRecord = new ProducerRecord<>("initiate-fund-transfer", "123456", new TransactionRQMessage());
        RecordMetadata recordMetadata = new RecordMetadata(null, 0, 0, 0L, 0L, 0, 0);
        when(sendResult.getProducerRecord()).thenReturn(producerRecord);
        when(sendResult.getRecordMetadata()).thenReturn(recordMetadata);
        when(fundTxnPublisher.publishAsync(any())).thenReturn(CompletableFuture.completedFuture(sendResult));

        Mono<TransactionRS> result = transactionService.doDebitTransaction(transactionRQ);

        StepVerifier.create(result)
                .expectNextMatches(transactionRS -> transactionRS.getStatus() == TransactionRS.Status.SUCCESS)
                .verifyComplete();
    }

    @Test
    void doDebitTransaction_shouldReturnError_whenAccountNotFound() {
        String accountId = "12345";
        BigDecimal amount = BigDecimal.valueOf(100.0);
        TransactionRQ transactionRQ = new TransactionRQ();
        transactionRQ.setAccountId(accountId);
        transactionRQ.setAmount(amount);
        transactionRQ.setCurrency(Currency.AED);

        when(accountDetailRepository.findByAccountId(accountId)).thenReturn(Mono.empty());

        Mono<TransactionRS> result = transactionService.doDebitTransaction(transactionRQ);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof InvalidRequestException && throwable.getMessage().equals("Account not found"))
                .verify();
    }

    @Test
    void doDebitTransaction_shouldReturnError_whenTransactionDetailsNotSaved() {
        String accountId = "12345";
        BigDecimal amount = BigDecimal.valueOf(100.0);
        TransactionRQ transactionRQ = new TransactionRQ();
        transactionRQ.setAccountId(accountId);
        transactionRQ.setAmount(amount);
        transactionRQ.setCurrency(Currency.AED);

        AccountDetail accountDetail = new AccountDetail();
        accountDetail.setAccountId(accountId);
        accountDetail.setBalance(amount);

        when(accountDetailRepository.findByAccountId(accountId)).thenReturn(Mono.just(accountDetail));

        when(transactionDetailsRepository.save(any())).thenReturn(Mono.empty());

        Mono<TransactionRS> result = transactionService.doDebitTransaction(transactionRQ);

        StepVerifier.create(result)
                .expectNextMatches(transactionRS -> transactionRS.getStatus() == TransactionRS.Status.FAILED)
                .verifyComplete();
    }

    @Test
    void doDebitTransaction_shouldReturnError_whenFundTransferEventNotPublished() {
        String accountId = "12345";
        BigDecimal amount = BigDecimal.valueOf(100.0);
        TransactionRQ transactionRQ = new TransactionRQ();
        transactionRQ.setAccountId(accountId);
        transactionRQ.setAmount(amount);
        transactionRQ.setCurrency(Currency.AED);

        AccountDetail accountDetail = new AccountDetail();
        accountDetail.setAccountId(accountId);
        accountDetail.setBalance(amount);

        when(accountDetailRepository.findByAccountId(accountId)).thenReturn(Mono.just(accountDetail));
        when(accountDetailRepository.save(accountDetail)).thenReturn(Mono.just(accountDetail));

        when(transactionDetailsRepository.save(any())).thenReturn(Mono.just(TransactionDetails.builder().build()));
        when(fundTxnPublisher.publishAsync(any())).thenReturn(CompletableFuture.failedFuture(new RuntimeException()));

        Mono<TransactionRS> result = transactionService.doDebitTransaction(transactionRQ);

        StepVerifier.create(result)
                .expectNextMatches(transactionRS -> transactionRS.getStatus() == TransactionRS.Status.FAILED)
                .verifyComplete();
    }

    @Test
    void doCreditTransaction_shouldReturnSuccess_whenAccountFoundAndTransactionSuccessful() {
        String accountId = "12345";
        BigDecimal amount = BigDecimal.valueOf(100.0);
        TransactionRQ transactionRQ = new TransactionRQ();
        transactionRQ.setAccountId(accountId);
        transactionRQ.setAmount(amount);
        transactionRQ.setCurrency(Currency.AED);

        AccountDetail accountDetail = new AccountDetail();
        accountDetail.setAccountId(accountId);
        accountDetail.setBalance(amount);

        when(accountDetailRepository.findByAccountId(accountId)).thenReturn(Mono.just(accountDetail));
        when(accountDetailRepository.save(accountDetail)).thenReturn(Mono.just(accountDetail));

        when(transactionDetailsRepository.save(any())).thenReturn(Mono.just(TransactionDetails.builder().build()));

        SendResult<String, TransactionRQMessage> sendResult = mock(SendResult.class);
        ProducerRecord<String, TransactionRQMessage> producerRecord = new ProducerRecord<>("initiate-fund-transfer", "123456", new TransactionRQMessage());
        RecordMetadata recordMetadata = new RecordMetadata(null, 0, 0, 0L, 0L, 0, 0);
        when(sendResult.getProducerRecord()).thenReturn(producerRecord);
        when(sendResult.getRecordMetadata()).thenReturn(recordMetadata);
        when(fundTxnPublisher.publishAsync(any())).thenReturn(CompletableFuture.completedFuture(sendResult));

        Mono<TransactionRS> result = transactionService.doCreditTransaction(transactionRQ);

        StepVerifier.create(result)
                .expectNextMatches(transactionRS -> transactionRS.getStatus() == TransactionRS.Status.SUCCESS)
                .verifyComplete();
    }

    @Test
    void doCreditTransaction_shouldReturnError_whenAccountNotFound() {
        String accountId = "12345";
        BigDecimal amount = BigDecimal.valueOf(100.0);
        TransactionRQ transactionRQ = new TransactionRQ();
        transactionRQ.setAccountId(accountId);
        transactionRQ.setAmount(amount);
        transactionRQ.setCurrency(Currency.AED);

        when(accountDetailRepository.findByAccountId(accountId)).thenReturn(Mono.empty());

        Mono<TransactionRS> result = transactionService.doCreditTransaction(transactionRQ);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof InvalidRequestException && throwable.getMessage().equals("Account not found"))
                .verify();
    }

    @Test
    void doCreditTransaction_shouldReturnError_whenTransactionDetailsNotSaved() {
        String accountId = "12345";
        BigDecimal amount = BigDecimal.valueOf(100.0);
        TransactionRQ transactionRQ = new TransactionRQ();
        transactionRQ.setAccountId(accountId);
        transactionRQ.setAmount(amount);
        transactionRQ.setCurrency(Currency.AED);

        AccountDetail accountDetail = new AccountDetail();
        accountDetail.setAccountId(accountId);
        accountDetail.setBalance(amount);

        when(accountDetailRepository.findByAccountId(accountId)).thenReturn(Mono.just(accountDetail));

        when(transactionDetailsRepository.save(any())).thenReturn(Mono.empty());

        Mono<TransactionRS> result = transactionService.doCreditTransaction(transactionRQ);

        StepVerifier.create(result)
                .expectNextMatches(transactionRS -> transactionRS.getStatus() == TransactionRS.Status.FAILED)
                .verifyComplete();
    }

    @Test
    void doCreditTransaction_shouldReturnError_whenFundTransferEventNotPublished() {
        String accountId = "12345";
        BigDecimal amount = BigDecimal.valueOf(100.0);
        TransactionRQ transactionRQ = new TransactionRQ();
        transactionRQ.setAccountId(accountId);
        transactionRQ.setAmount(amount);
        transactionRQ.setCurrency(Currency.AED);

        AccountDetail accountDetail = new AccountDetail();
        accountDetail.setAccountId(accountId);
        accountDetail.setBalance(amount);

        when(accountDetailRepository.findByAccountId(accountId)).thenReturn(Mono.just(accountDetail));
        when(accountDetailRepository.save(accountDetail)).thenReturn(Mono.just(accountDetail));

        when(transactionDetailsRepository.save(any())).thenReturn(Mono.just(TransactionDetails.builder().build()));
        when(fundTxnPublisher.publishAsync(any())).thenReturn(CompletableFuture.failedFuture(new RuntimeException()));

        Mono<TransactionRS> result = transactionService.doCreditTransaction(transactionRQ);

        StepVerifier.create(result)
                .expectNextMatches(transactionRS -> transactionRS.getStatus() == TransactionRS.Status.FAILED)
                .verifyComplete();
    }

    @Test
    void rollbackTransaction_shouldRollbackDebitTransaction_whenCalled() {
        String referenceNo = "12345";
        BigDecimal amount = BigDecimal.valueOf(100);
        String accountId = "67890";
        String transactionType = "DEBIT";
        String transactionStatus = "FAILED";
        String errorDescription = "Transaction failed";

        TransactionRSMessage message = new TransactionRSMessage();
        message.setReferenceNo(referenceNo);
        message.setStatus(transactionStatus);
        message.setErrorDescription(errorDescription);

        TransactionDetails expectedDetails = TransactionDetails.builder()
                .referenceNo(referenceNo)
                .accountId(accountId)
                .amount(amount)
                .currency("USD")
                .description("Test transaction")
                .transactionType(transactionType)
                .transactionStatus(transactionStatus)
                .build();
        when(transactionDetailsRepository.findByReferenceNo(referenceNo)).thenReturn(Mono.just(expectedDetails));
        when(transactionDetailsRepository.save(expectedDetails)).thenReturn(Mono.just(expectedDetails));

        AccountDetail accountDetail = new AccountDetail();
        accountDetail.setBalance(new BigDecimal(100));
        when(accountDetailRepository.findByAccountId(accountId)).thenReturn(Mono.just(accountDetail));

        transactionService.rollbackTransaction(message);

        assertEquals(new BigDecimal(200), accountDetail.getBalance());
    }

    @Test
    void rollbackTransaction_shouldRollbackCreditTransaction_whenCalled() {
        String referenceNo = "12345";
        BigDecimal amount = BigDecimal.valueOf(200);
        String accountId = "67890";
        String transactionType = "CREDIT";
        String transactionStatus = "FAILED";
        String errorDescription = "Transaction failed";

        TransactionRSMessage message = new TransactionRSMessage();
        message.setReferenceNo(referenceNo);
        message.setStatus(transactionStatus);
        message.setErrorDescription(errorDescription);

        TransactionDetails expectedDetails = TransactionDetails.builder()
                .referenceNo(referenceNo)
                .accountId(accountId)
                .amount(amount)
                .currency("USD")
                .description("Test transaction")
                .transactionType(transactionType)
                .transactionStatus(transactionStatus)
                .build();
        when(transactionDetailsRepository.findByReferenceNo(referenceNo)).thenReturn(Mono.just(expectedDetails));
        when(transactionDetailsRepository.save(expectedDetails)).thenReturn(Mono.just(expectedDetails));

        AccountDetail accountDetail = new AccountDetail();
        accountDetail.setBalance(new BigDecimal(300));
        when(accountDetailRepository.findByAccountId(accountId)).thenReturn(Mono.just(accountDetail));

        transactionService.rollbackTransaction(message);

        assertEquals(new BigDecimal(100), accountDetail.getBalance());
    }

    @Test
    void updateTransactionDetails_shouldUpdateTransactionDetails_whenCalled() {
        String referenceNo = "12345";
        String transactionStatus = "SUCCESS";

        TransactionRSMessage message = new TransactionRSMessage();
        message.setReferenceNo(referenceNo);
        message.setStatus(transactionStatus);

        TransactionDetails expectedDetails = TransactionDetails.builder()
                .referenceNo(referenceNo)
                .accountId("67890")
                .amount(new BigDecimal(100.0))
                .currency("USD")
                .description("Test transaction")
                .transactionType("DEBIT")
                .transactionStatus(transactionStatus)
                .build();
        when(transactionDetailsRepository.findByReferenceNo(referenceNo)).thenReturn(Mono.just(expectedDetails));
        when(transactionDetailsRepository.save(expectedDetails)).thenReturn(Mono.just(expectedDetails));

        transactionService.updateTransactionDetails(message);
    }
}