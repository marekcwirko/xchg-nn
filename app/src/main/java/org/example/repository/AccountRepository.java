package org.example.repository;

import org.example.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByAccountIdentifier(String accountIdentifier);

    default Account findByAccountIdentifierOrThrow(String accountIdentifier) {
        return Optional.of(findByAccountIdentifier(accountIdentifier))
                .orElseThrow(() -> new NoAccountForIdentifierException("Account with identifier " + accountIdentifier + " not found"));
    }
}
