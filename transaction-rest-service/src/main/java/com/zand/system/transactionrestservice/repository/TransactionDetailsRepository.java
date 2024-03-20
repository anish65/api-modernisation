package com.zand.system.transactionrestservice.repository;

import com.zand.system.transactionrestservice.entity.TransactionDetails;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface TransactionDetailsRepository extends ReactiveCrudRepository<TransactionDetails, Long> {

    Mono<TransactionDetails> findByReferenceNo(String referenceNo);

}
