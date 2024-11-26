package org.example.monitoring

import org.junit.jupiter.api.Disabled
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import spock.lang.Specification
import spock.lang.Stepwise

import static io.restassured.RestAssured.given

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Stepwise
class HealthEndpointSpec extends Specification {

    @LocalServerPort
    int port

    def "should return health status as UP and include custom health details"() {
        when: "The /actuator/health endpoint is called"
        def response = given()
                .port(port)
                .basePath("/actuator/health")
                .when()
                .get()

        then: "The response status is OK"
        response.statusCode() == 200 // HttpStatus.OK.value()

        and: "The response contains health status as UP"
        println response.body().asString()
        response.jsonPath().getString("status") == "UP"

        and: "The response contains custom health indicator details"
        response.jsonPath().getString("components.custom.status") == "UP"
        response.jsonPath().getString("components.custom.details.healthIndicator") == "Everything is fine"
    }
}
