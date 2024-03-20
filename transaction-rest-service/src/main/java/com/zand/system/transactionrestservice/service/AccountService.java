package com.zand.system.transactionrestservice.service;

import com.zand.system.transactionrestservice.dto.AccountBalanceRS;
import com.zand.system.transactionrestservice.dto.Currency;
import com.zand.system.transactionrestservice.entity.AccountDetail;
import com.zand.system.transactionrestservice.repository.AccountDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountDetailRepository accountDetailRepository;

    public Mono<AccountBalanceRS> getAccountBalance(String accountId) {
        return accountDetailRepository.findByAccountId(accountId)
                .flatMap(accountDetail -> {
                    AccountBalanceRS accountBalanceRS = new AccountBalanceRS();
                    accountBalanceRS.setCifId(accountDetail.getCifId());
                    accountBalanceRS.setAccountId(accountDetail.getAccountId());
                    accountBalanceRS.setBalance(accountDetail.getBalance());
                    accountBalanceRS.setCurrency(Currency.valueOf(accountDetail.getCurrency()));
                    return Mono.just(accountBalanceRS);
                }).switchIfEmpty(Mono.error(new RuntimeException("Account not found")));
    }

    public Mono<AccountDetail> createAccount(AccountDetail accountDetail) {
        return accountDetailRepository.save(accountDetail);
    }

}
