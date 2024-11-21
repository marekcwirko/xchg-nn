package org.example.web;

import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import spock.lang.Specification;

@WebFluxTest(WebController)
class WebControllerSpec extends Specification {

    WebTestClient webTestClient;

    def setup() {
        webTestClient = WebTestClient
                .bindToController(new WebController())
                .build()
    }

    def "home endpoint should return home message"() {
        given:
        def body = "OK"

        when:
        def response = webTestClient.get()
                .uri { uriBuilder ->
                    uriBuilder.path("/api/health").build()
                }
                .accept(MediaType.APPLICATION_JSON)
                .exchange()

        then:
        response.expectStatus().isOk()
        response.expectBody().jsonPath('$').isEqualTo(body)
    }
}