package org.example.web

import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import spock.lang.Specification

@WebFluxTest(AccountController)
class AccountControllerSpec extends Specification {

    WebTestClient webTestClient

    def setup() {
        webTestClient = WebTestClient
                .bindToController(new AccountController())
                .build()
    }

    def "should create an account and return the created account"() {
        given:
        def account = "OK, create in accountService"

        when:
        def response = webTestClient.post()
                .uri { uriBuilder ->
                    uriBuilder.path("/api/accounts")
                            .queryParam("firstName", "John")
                            .queryParam("lastName", "Doe")
                            .queryParam("initialBalance", 1000.0)
                            .build()
                }
                .accept(MediaType.APPLICATION_JSON)
                .exchange()

        then:
        response.expectStatus().isOk()
        response.expectBody().jsonPath('$').isEqualTo(account)
    }

    def "should fetch an account by id"() {
        given:
        def account = "OK, call accountService"

        when:
        def response = webTestClient.get()
                .uri("/api/accounts/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()

        then:
        response.expectStatus().isOk()
        response.expectBody().jsonPath('$').isEqualTo(account)
    }

    def "should fetch the balance in USD for an account"() {
        given:
        def usdBalance = 0.0

        when:
        def response = webTestClient.get()
                .uri("/api/accounts/1/balance-usd")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()

        then:
        response.expectStatus().isOk()
        response.expectBody().jsonPath('$').isEqualTo(usdBalance)
    }
}
