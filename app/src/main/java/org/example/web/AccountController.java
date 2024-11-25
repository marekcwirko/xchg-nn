package org.example.web;

import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.model.Account;
import org.example.service.AccountService;
import org.example.service.ExchangeRateUnavailableException;
import org.example.service.ExchangeService;
import org.example.web.dto.CreateAccountRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class AccountController {

    private final AccountService accountService;
    private final ExchangeService exchangeService;

    @PostMapping
    public ResponseEntity<Account> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        Account createdAccount = accountService.createAccount(
                request.getName(),
                request.getSurname(),
                request.getBalancePLN());
        return ResponseEntity.ok(createdAccount);
    }

    @GetMapping("/{accountIdentifier}")
    public ResponseEntity<Account> getAccount(@PathVariable String accountIdentifier) {
        return Optional.ofNullable(accountService.getAccount(accountIdentifier))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{accountIdentifier}/balance-usd")
    public ResponseEntity<Double> getBalanceInUSD(@PathVariable String accountIdentifier) {
        return Optional.ofNullable(accountService.getBalanceInUSD(accountIdentifier))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{accountIdentifier}/exchange")
    public Account exchangeCurrency(@PathVariable String accountIdentifier, @RequestParam Double amount, @RequestParam boolean isPlnToUsd) {
        Double exchangeRate = exchangeService.getExchangeRatePLNtoUSD()
                .orElseThrow(() -> new ExchangeRateUnavailableException("Exchange rate is unavailable"));
        return accountService.exchangeCurrency(accountIdentifier, amount, isPlnToUsd, exchangeRate);
    }
}

