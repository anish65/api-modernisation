package com.zand.system.transactionrestservice.repository;

import com.zand.system.transactionrestservice.entity.AccountDetail;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;


public interface AccountDetailRepository extends ReactiveCrudRepository<AccountDetail, Long> {

    Mono<AccountDetail> findByAccountId(String accountId);

}
