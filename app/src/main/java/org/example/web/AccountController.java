package org.example.web;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.model.Account;
import org.example.service.AccountService;
import org.example.service.ExchangeService;
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
    public ResponseEntity<Account> createAccount(@RequestParam String firstName,
                                                 @RequestParam String lastName,
                                                 @RequestParam Double initialBalance) {
        return ResponseEntity.ok(accountService.createAccount(firstName, lastName, initialBalance));
    }

    @GetMapping("/{accountIdentifier}")
    public ResponseEntity<Account> getAccount(@PathVariable String accountIdentifier) {
        return Optional.of(accountService.getAccount(accountIdentifier))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{accountIdentifier}/balance-usd")
    public ResponseEntity<Double> getBalanceInUSD(@PathVariable String accountIdentifier) {
        return ResponseEntity.ok(accountService.getBalanceInUSD(accountIdentifier));
    }

    @PostMapping("/{accountIdentifier}/exchange")
    public Account exchangeCurrency(@PathVariable String accountIdentifier, @RequestParam Double amount, @RequestParam boolean isPlnToUsd) {
        Double exchangeRate = exchangeService.getExchangeRatePLNtoUSD();
        return accountService.exchangeCurrency(accountIdentifier, amount, isPlnToUsd, exchangeRate);
    }
}

