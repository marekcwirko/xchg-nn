package org.example.web

import org.example.model.Account
import org.example.model.NBPResponse
import org.example.service.AccountService
import org.example.service.ExchangeService
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.web.client.RestTemplate
import spock.lang.Specification
import spock.lang.Unroll

@SpringBootTest
class AccountControllerSpec extends Specification {

    def restTemplate = Mock(RestTemplate)
    def exchangeService = new ExchangeService(restTemplate)
    def accountService = Mock(AccountService)
    def accountController = new AccountController(accountService, exchangeService)

    @Unroll
    def "should return #expectedStatus if account balance in USD is #balanceUSD for identifier #accountIdentifier"() {
        given:
        accountService.getBalanceInUSD(accountIdentifier) >> balanceUSD

        when:
        def response = accountController.getBalanceInUSD(accountIdentifier)

        then:
        response.statusCode == expectedStatus
        response.body == expectedBody

        where:
        accountIdentifier   | balanceUSD | expectedStatus        | expectedBody
        "existing-id"       | 123.45     | HttpStatus.OK         | 123.45
        "non-existent-id"   | null       | HttpStatus.NOT_FOUND  | null
    }

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

    @Unroll
    def "should return #expectedStatus when account #description"() {
        given:
        accountService.getAccount(accountIdentifier) >> account

        when:
        def response = accountController.getAccount(accountIdentifier)

        then:
        response.statusCode == expectedStatus
        response.body == expectedBody

        where:
        accountIdentifier   | account   || expectedStatus   | expectedBody  | description
        "existing-id"       | new Account(accountIdentifier: "existing-id", balancePLN: 1000.0) || HttpStatus.OK       | new Account(accountIdentifier: "existing-id", balancePLN: 1000.0) | "exists"
        "non-existent-id"   | null  || HttpStatus.NOT_FOUND  | null | "does not exist"
    }

    @Unroll
    def "should return #expectedStatus when balance in USD for account #accountIdentifier is #balanceUSD"() {
        given:
        accountService.getBalanceInUSD(accountIdentifier) >> balanceUSD

        when:
        def response = accountController.getBalanceInUSD(accountIdentifier)

        then:
        response.statusCode == expectedStatus
        response.body == expectedBody

        where:
        accountIdentifier   | balanceUSD | expectedStatus        | expectedBody
        "existing-id"       | 123.45     | HttpStatus.OK         | 123.45
        "non-existent-id"   | null       | HttpStatus.NOT_FOUND  | null
    }

    @Unroll
    def "should return #expectedResult when response is #description"() {
        given:
        restTemplate.getForObject(_, _) >> mockResponse

        when:
        def result = exchangeService.getExchangeRatePLNtoUSD()

        then:
        result.isPresent() == isPresent
        result.orElse(null) == expectedResult

        where:
        description                 | mockResponse                                || isPresent | expectedResult
        "valid with exchange rate"  | new NBPResponse(rates: [new NBPResponse.Rate(mid: 4.5)]) || true | 4.5
        "null response"             | null                                        || false     | null
        "empty rates list"          | new NBPResponse(rates: [])                  || false     | null
    }

}
