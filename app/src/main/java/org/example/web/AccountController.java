package org.example.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    @PostMapping
    public ResponseEntity<String> createAccount(@RequestParam String firstName,
                                                 @RequestParam String lastName,
                                                 @RequestParam Double initialBalance) {
        return ResponseEntity.ok("OK, create in accountService");
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getAccount(@PathVariable Long id) {
        return ResponseEntity.ok("OK, call accountService");
    }

    @GetMapping("/{id}/balance-usd")
    public ResponseEntity<String> getBalanceInUSD(@PathVariable Long id) {
        return ResponseEntity.ok("0.0");
    }
}

