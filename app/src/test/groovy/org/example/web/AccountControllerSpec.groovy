package org.example.web

import org.example.model.Account
import org.example.model.NBPResponse
import org.example.service.AccountService
import org.example.service.ExchangeService
import org.example.web.dto.CreateAccountRequest
import org.springframework.http.MediaType
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.client.RestTemplate
import spock.lang.Specification
import spock.lang.Unroll

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

@SpringBootTest
class AccountControllerSpec extends Specification {

    def restTemplate = Mock(RestTemplate)
    def exchangeService = new ExchangeService(restTemplate)
    def accountService = Mock(AccountService)
    def requestValidator = Mock(RequestValidator)
    def accountController = new AccountController(accountService, exchangeService)
    MockMvc mockMvc

    def setup() {
        mockMvc = standaloneSetup(accountController).build()
    }

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

    def "should create an account and return the created account"() {
        given: "A valid CreateAccountRequest"
        def account = new Account(id: 1L, name: "John", surname: "Doe", balancePLN: 1000.0, balanceUSD: null)
        accountService.createAccount("John", "Doe", 1000.0) >> account

        when: "The createAccount endpoint is called"
        def response = mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "name": "John",
                        "surname": "Doe",
                        "balancePLN": 1000.0
                    }
                """)
        )

        then: "The response is successful, and the account is returned"
        response.andExpect(status().isOk())
        1 * accountService.createAccount("John", "Doe", 1000.0)
    }

    def "should return bad request when validation fails"() {
        given: "An invalid CreateAccountRequest"
        def invalidJson = """
            {
                "name": "",
                "surname": "Doe",
                "balancePLN": -500.0
            }
        """
        requestValidator.validateAccountCreateRequest(_ as CreateAccountRequest) >> { throw new IllegalArgumentException("Invalid request") }

        when: "The createAccount endpoint is called with invalid data"
        def response = mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson)
        )

        then: "The response is a bad request"
        response.andExpect(status().isBadRequest())
        0 * accountService.createAccount(_, _, _)
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
