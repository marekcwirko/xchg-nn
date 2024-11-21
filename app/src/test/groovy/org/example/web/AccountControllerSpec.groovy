package org.example.web

import org.example.model.Account
import org.example.service.AccountService
import org.example.service.ExchangeService
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Unroll

@SpringBootTest
class AccountControllerSpec extends Specification {

    def accountService = Mock(AccountService)
    def exchangeService = Mock(ExchangeService)
    def accountController = new AccountController(accountService, exchangeService)

    @Unroll
    def "should create account with name: #name, surname: #surname, balancePLN: #balancePLN"() {
        given:
        def expectedAccount = new Account(name: name, surname: surname, balancePLN: balancePLN, accountIdentifier: "test-id")

        when:
        def result = accountController.createAccount(name, surname, balancePLN)

        then:
        1 * accountService.createAccount(name, surname, balancePLN) >> expectedAccount
        result.body == expectedAccount

        where:
        name | surname | balancePLN
        "John" | "Doe" | 1000.0
    }

    def "should get account by accountIdentifier"() {
        given:
        def accountIdentifier = "test-id"
        def expectedAccount = new Account(accountIdentifier: accountIdentifier, balancePLN: 1000.0)

        when:
        def result = accountController.getAccount(accountIdentifier)

        then:
        1 * accountService.getAccount(accountIdentifier) >> expectedAccount
        result.body == expectedAccount
    }

    def "should exchange currency"() {
        given:
        def accountIdentifier = "test-id"
        def amount = 100.0
        def isPlnToUsd = true
        def exchangeRate = 3.0
        def expectedAccount = new Account(accountIdentifier: accountIdentifier, balancePLN: 600.0)

        when:
        def result = accountController.exchangeCurrency(accountIdentifier, amount, isPlnToUsd)

        then:
        1 * exchangeService.getExchangeRatePLNtoUSD() >> exchangeRate
        1 * accountService.exchangeCurrency(accountIdentifier, amount, isPlnToUsd, exchangeRate) >> expectedAccount
        result == expectedAccount
    }
}
