package org.example.service

import org.example.model.Account
import org.example.repository.AccountRepository
import org.example.repository.NoAccountForIdentifierException
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class AccountServiceSpec extends Specification {

    def accountRepository = Mock(AccountRepository)
    def exchengeService = Mock(ExchangeService)

    @Subject
    AccountService accountService = new AccountService(accountRepository, exchengeService)

    def "should create a new account"() {
        given:
        def name = "John"
        def surname = "Doe"
        def balancePLN = 1000.0
        def generatedUUID = "322e1237-e23b-18a3-e426-127714174432"
        def expectedAccount = new Account(
                name: name,
                surname: surname,
                balancePLN: balancePLN,
                accountIdentifier: generatedUUID
        )

        when:
        def result = accountService.createAccount(name, surname, balancePLN)

        then:
        1 * accountRepository.save(_) >> { Account account ->
            assert account.name == name
            assert account.surname == surname
            assert account.balancePLN == balancePLN
            account.accountIdentifier = generatedUUID
            account
        }
        result == expectedAccount
    }

    def "should return account if found"() {
        given:
        def accountIdentifier = "test-id"
        def expectedAccount = new Account(accountIdentifier: accountIdentifier, balancePLN: 1000.0)

        when:
        def result = accountService.getAccount(accountIdentifier)

        then:
        1 * accountRepository.findByAccountIdentifierOrThrow(accountIdentifier) >> expectedAccount
        result == expectedAccount
    }

    def "should return account if found"() {
        given:
        def accountIdentifier = "test-id"
        def expectedAccount = new Account(accountIdentifier: accountIdentifier, balancePLN: 1000.0)

        when:
        def result = accountService.getAccount(accountIdentifier)

        then:
        1 * accountRepository.findByAccountIdentifierOrThrow(accountIdentifier) >> expectedAccount
        result == expectedAccount
    }

    @Unroll
    def "should exchange currency correctly for PLN to USD and USD to PLN"() {
        given:
        def accountIdentifier = "test-id"
        def initialBalancePLN = 1000.0
        def amount = amountInput
        def exchangeRate = 4.0
        def account = new Account(accountIdentifier: accountIdentifier, balancePLN: initialBalancePLN)

        when:
        def result = accountService.exchangeCurrency(accountIdentifier, amount, isPlnToUsd, exchangeRate)

        then:
        1 * accountRepository.findByAccountIdentifierOrThrow(accountIdentifier) >> account
        1 * accountRepository.save(_) >> { Account updatedAccount ->
            assert updatedAccount.balancePLN == expectedPLNBalance
            updatedAccount
        }
        result.balancePLN == expectedPLNBalance

        where:
        isPlnToUsd | amountInput || expectedPLNBalance
        true       | 100.0       || 900.0
        false      | 100.0       || 1025.0
    }

    def "should return account when found"() {
        given:
        def accountIdentifier = "existing-id"
        def expectedAccount = new Account(accountIdentifier: accountIdentifier, balancePLN: 1000.0)

        when:
        def result = accountService.getAccount(accountIdentifier)

        then:
        1 * accountRepository.findByAccountIdentifierOrThrow(accountIdentifier) >> expectedAccount
        result == expectedAccount
    }

    def "should throw NoSuchElementException when account not found"() {
        given:
        def accountIdentifier = "non-existent-id"

        when:
        accountService.getAccount(accountIdentifier)

        then:
        1 * accountRepository.findByAccountIdentifierOrThrow(accountIdentifier) >> { throw new NoAccountForIdentifierException("Account with identifier " + accountIdentifier + " not found") }
        thrown(NoAccountForIdentifierException)
    }
}