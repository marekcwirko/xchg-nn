package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.Account;
import org.example.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final ExchangeService exchangeService;

    public Account createAccount(String name, String surname, Double initialBalance) {
        Account account = Account.builder()
                .name(name)
                .surname(surname)
                .accountIdentifier(UUID.randomUUID().toString())
                .balancePLN(initialBalance)
                .build();
        return accountRepository.save(account);
    }

    public Account getAccount(String accountIdentifier) {
        return accountRepository.findByAccountIdentifierOrThrow(accountIdentifier);
    }

    public Double getBalanceInUSD(String accountIdentifier) {
        Account account = accountRepository.findByAccountIdentifierOrThrow(accountIdentifier);
        return exchangeService.getExchangeRatePLNtoUSD()
                .map(exchangeRate -> account.getBalancePLN() / exchangeRate)
                .orElseThrow(ExchangeRateUnavailableException::new);
    }

    public Account exchangeCurrency(String accountIdentifier, Double amount, boolean isPlnToUsd, Double exchangeRate) {
        Account account = getAccount(accountIdentifier);
        if (isPlnToUsd) {
            account.setBalancePLN(account.getBalancePLN() - amount);
        } else {
            account.setBalancePLN(account.getBalancePLN() + amount / exchangeRate);
        }
        return accountRepository.save(account);
    }
}
