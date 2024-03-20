package com.zand.system.transactionrestservice.service;

import com.zand.system.common.messaging.kafka.message.TransactionRQMessage;
import com.zand.system.common.messaging.kafka.message.TransactionRSMessage;
import com.zand.system.transactionrestservice.builder.TransactionEventMsgBuilder;
import com.zand.system.transactionrestservice.builder.TransactionDetailEntityBuilder;
import com.zand.system.transactionrestservice.dto.TransactionRS;
import com.zand.system.transactionrestservice.dto.TransactionRQ;
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

import static com.zand.system.transactionrestservice.dto.TransactionType.CREDIT;
import static com.zand.system.transactionrestservice.dto.TransactionType.DEBIT;


@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final FundTxnPublisher fundTxnPublisher;

    private final AccountDetailRepository accountDetailRepository;
    private final TransactionDetailsRepository transactionDetailsRepository;

    public Mono<TransactionRS> doDebitTransaction(TransactionRQ transactionRQ) {
        return accountDetailRepository.findByAccountId(transactionRQ.getAccountId())
                .switchIfEmpty(Mono.error(new InvalidRequestException("Account not found")))
                .flatMap(accountDetails -> {
                    if (transactionRQ.getAmount().compareTo(accountDetails.getBalance()) > 0) {
                        return Mono.error(new RuntimeException("Insufficient balance"));
                    } else {
                        // Create a FundTransferMessage
                        return saveAccountDetailsAndCreatePublishMsg(transactionRQ, accountDetails, DEBIT)
                                .switchIfEmpty(Mono.error(new InvalidRequestException("Exception while saving transaction details")))
                                .flatMap(transactionRQMessage -> {
                                    // Publish the fund transfer message asynchronously
                                    return publishFundTransferEvent(transactionRQMessage, accountDetails, DEBIT);
                                });
                    }
                });
    }

    public Mono<TransactionRS> doCreditTransaction(TransactionRQ transactionRQ) {
        return accountDetailRepository.findByAccountId(transactionRQ.getAccountId())
                .switchIfEmpty(Mono.error(new InvalidRequestException("Account not found")))
                .flatMap(accountDetails -> saveAccountDetailsAndCreatePublishMsg(transactionRQ, accountDetails, CREDIT)
                        .switchIfEmpty(Mono.error(new InvalidRequestException("Exception while saving transaction details")))
                        .flatMap(transactionRQMessage -> {
                            // Publish the fund transfer message asynchronously
                            return publishFundTransferEvent(transactionRQMessage, accountDetails, CREDIT);
                        })
                );
    }


    private Mono<TransactionRQMessage> saveAccountDetailsAndCreatePublishMsg(TransactionRQ transactionRQ,
                                                                             AccountDetail accountDetail,TransactionType transactionType) {
        TransactionRQMessage transactionRQMessage = TransactionEventMsgBuilder.mapToFundTransferMessage(transactionRQ, transactionType);
        // Save the transaction to database account balance
        TransactionDetails transactionDetails = TransactionDetailEntityBuilder.buildTransactionEntity(transactionRQMessage);
        // Deduct the amount from the account balance
        transactionDetailsRepository.save(transactionDetails).doOnNext(transactionDetail -> {
            // Save the updated account balance
            switch (transactionType) {
                case DEBIT -> accountDetail.setBalance(accountDetail.getBalance().subtract(transactionRQ.getAmount()));
                case CREDIT -> accountDetail.setBalance(accountDetail.getBalance().add(transactionRQ.getAmount()));
                default -> throw new IllegalArgumentException("Invalid transaction Type");
            }
            accountDetailRepository.save(accountDetail).subscribe();
        }).subscribe();
        return Mono.just(transactionRQMessage);
    }

    private Mono<TransactionRS> publishFundTransferEvent(TransactionRQMessage transactionRQMessage,
                                                         AccountDetail accountDetail,
                                                         TransactionType transactionType) {
        // Publish the fund transfer message asynchronously
        return Mono.fromFuture(() -> fundTxnPublisher.publishAsync(transactionRQMessage)).flatMap(sendResult -> {
                    // Successful Kafka publish
                    log.info("Successfully published message to Kafka with payment id: {}", transactionRQMessage.getReferenceNo());
                    return Mono.just(new TransactionRS(transactionRQMessage.getReferenceNo(), TransactionRS.Status.SUCCESS));
                }).onErrorResume(error -> {
                    // Error while publishing to Kafka
                    log.error("Error while publishing message to Kafka with payment id", error);
                    // Rollback the account balance
                    switch (transactionType) {
                        case DEBIT -> accountDetail.setBalance(accountDetail.getBalance().add(transactionRQMessage.getAmount()));
                        case CREDIT -> accountDetail.setBalance(accountDetail.getBalance().subtract(transactionRQMessage.getAmount()));
                        default -> throw new IllegalArgumentException("Invalid transaction Type");
                    }
                    // Save the updated account balance again
                    return Mono.just(new TransactionRS(transactionRQMessage.getReferenceNo(), TransactionRS.Status.FAILED));
                });
    }

    public void rollbackTransaction(TransactionRSMessage message) {
        transactionDetailsRepository.findByReferenceNo(message.getReferenceNo())
                .subscribe(transactionDetail -> {
                    accountDetailRepository.findByAccountId(transactionDetail.getAccountId())
                            .subscribe(accountDetail -> {
                                switch (transactionDetail.getTransactionType()) {
                                    case "DEBIT" -> accountDetail.setBalance(accountDetail.getBalance().add(transactionDetail.getAmount()));
                                    case "CREDIT" -> accountDetail.setBalance(accountDetail.getBalance().subtract(transactionDetail.getAmount()));
                                    default -> throw new IllegalArgumentException("Invalid transaction Type");
                                }
                                accountDetailRepository.save(accountDetail).subscribe();
                            });
                    transactionDetail.setTransactionStatus(message.getStatus());
                    transactionDetail.setTransactionStatus(message.getErrorDescription());
                    transactionDetailsRepository.save(transactionDetail).subscribe();
                });
    }

    public void updateTransactionDetails(TransactionRSMessage message) {
        transactionDetailsRepository.findByReferenceNo(message.getReferenceNo())
                .subscribe(transactionDetail -> {
                    transactionDetail.setTransactionStatus(message.getStatus());
                    transactionDetailsRepository.save(transactionDetail).subscribe();
                });
    }
}
