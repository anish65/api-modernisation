package com.zand.system.transactionrestservice.service;

import com.zand.system.common.messaging.kafka.message.FundTransferRQMessage;
import com.zand.system.common.messaging.kafka.message.FundTransferRSMessage;
import com.zand.system.transactionrestservice.builder.FundTransferMessageBuilder;
import com.zand.system.transactionrestservice.builder.TransactionDetailEntityBuilder;
import com.zand.system.transactionrestservice.dto.FundTransferRQ;
import com.zand.system.transactionrestservice.dto.FundTransferRS;
import com.zand.system.transactionrestservice.dto.TransactionType;
import com.zand.system.transactionrestservice.entity.AccountDetail;
import com.zand.system.transactionrestservice.entity.TransactionDetails;
import com.zand.system.transactionrestservice.exception.InvalidRequestException;
import com.zand.system.transactionrestservice.messaging.producer.FundTxnPublisher;
import com.zand.system.transactionrestservice.repository.AccountDetailRepository;
import com.zand.system.transactionrestservice.repository.TransactionDetailsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final FundTxnPublisher fundTxnPublisher;

    private final AccountDetailRepository accountDetailRepository;
    private final TransactionDetailsRepository transactionDetailsRepository;

    public Mono<FundTransferRS> doFundTransferTransaction(FundTransferRQ fundTransferRQ) {
        return accountDetailRepository.findByAccountId(fundTransferRQ.getToAccountId())
                .switchIfEmpty(Mono.error(new InvalidRequestException("To account not found")))
                .flatMap(toAccountDetails -> accountDetailRepository.findByAccountId(fundTransferRQ.getFromAccountId())
                        .switchIfEmpty(Mono.error(new InvalidRequestException("From account not found")))
                        .flatMap(fromAccountDetails -> {
                            if (fundTransferRQ.getAmount().compareTo(fromAccountDetails.getBalance()) > 0) {
                                return Mono.error(new RuntimeException("Insufficient balance"));
                            } else {
                                // Create a FundTransferMessage
                                return saveAccountDetailsAndCreatePublishMsg(fundTransferRQ, fromAccountDetails, toAccountDetails)
                                        .switchIfEmpty(Mono.error(new InvalidRequestException("Exception while saving transaction details")))
                                        .flatMap(fundTransferMessage -> {
                                            // Publish the fund transfer message asynchronously
                                            return publishFundTransferEvent(fundTransferMessage, fromAccountDetails, toAccountDetails);
                                });
                            }
                        })
                );
    }

    private Mono<FundTransferRQMessage> saveAccountDetailsAndCreatePublishMsg(FundTransferRQ fundTransferRQ,
                                                                              AccountDetail fromAccountDetails,
                                                                              AccountDetail toAccountDetails) {
        FundTransferRQMessage fundTransferMessage = FundTransferMessageBuilder.mapToFundTransferMessage(fundTransferRQ);
        // Save the transaction to database account balance
        TransactionDetails transactionDetails = TransactionDetailEntityBuilder.buildTransactionEntity(fundTransferMessage);
        // Deduct the amount from the account balance
        transactionDetailsRepository.save(transactionDetails).doOnNext(transactionDetail -> {
            // Save the updated account balance
            fromAccountDetails.setBalance(fromAccountDetails.getBalance().subtract(fundTransferRQ.getAmount()));
            toAccountDetails.setBalance(toAccountDetails.getBalance().add(fundTransferRQ.getAmount()));
            accountDetailRepository.save(toAccountDetails).subscribe();
            accountDetailRepository.save(fromAccountDetails).subscribe();
        }).subscribe();
        return Mono.just(fundTransferMessage);
    }

    private Mono<FundTransferRS> publishFundTransferEvent(FundTransferRQMessage fundTransferMessage,
                                                          AccountDetail fromAccountDetails,
                                                          AccountDetail toAccountDetails) {
        // Publish the fund transfer message asynchronously
        return Mono.fromFuture(() -> fundTxnPublisher.publishAsync(fundTransferMessage)).flatMap(sendResult -> {
                    // Successful Kafka publish
                    log.info("Successfully published message to Kafka with payment id: {}", fundTransferMessage.getReferenceNo());
                    return Mono.just(new FundTransferRS(fundTransferMessage.getReferenceNo(), FundTransferRS.Status.SUCCESS));
                }).onErrorResume(error -> {
                    // Error while publishing to Kafka
                    log.error("Error while publishing message to Kafka with payment id", error);
                    // Rollback the account balance
                    fromAccountDetails.setBalance(fromAccountDetails.getBalance().add(fundTransferMessage.getAmount()));
                    toAccountDetails.setBalance(toAccountDetails.getBalance().subtract(fundTransferMessage.getAmount()));
                    // Save the updated account balance again
                    return Mono.just(new FundTransferRS(fundTransferMessage.getReferenceNo(), FundTransferRS.Status.FAILED));
                });
    }

    public void rollbackDebitTransaction(FundTransferRSMessage message) {
        transactionDetailsRepository.findByReferenceNo(message.getReferenceNo())
                .subscribe(transactionDetail -> {
                    accountDetailRepository.findByAccountId(transactionDetail.getFromAccountId())
                            .subscribe(fromAccountDetail -> {
                                fromAccountDetail.setBalance(fromAccountDetail.getBalance().add(transactionDetail.getAmount()));
                                accountDetailRepository.save(fromAccountDetail).subscribe();
                            });
                    accountDetailRepository.findByAccountId(transactionDetail.getToAccountId())
                            .subscribe(toAccountDetail -> {
                                toAccountDetail.setBalance(toAccountDetail.getBalance().subtract(transactionDetail.getAmount()));
                                accountDetailRepository.save(toAccountDetail).subscribe();
                            });
                    transactionDetail.setTransactionStatus(message.getStatus());
                    transactionDetail.setTransactionStatus(message.getErrorDescription());
                    transactionDetailsRepository.save(transactionDetail).subscribe();
                });
    }

    public void updateTransactionDetails(FundTransferRSMessage message) {
        transactionDetailsRepository.findByReferenceNo(message.getReferenceNo())
                .subscribe(transactionDetail -> {
                    transactionDetail.setTransactionStatus(message.getStatus());
                    transactionDetailsRepository.save(transactionDetail).subscribe();
                });
    }
}
