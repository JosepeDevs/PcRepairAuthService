package com.josepedevs.infra.input.rest.authenticationdata;

import com.josepedevs.domain.entity.AuthenticationData;
import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

@Slf4j
class RestAuthenticationDataControllerIT {

    private final String endpoint = "/api/v1/noauth/authenticate";

    private Map<String,String> headers;

    private final EasyRandom easyRandom = new EasyRandom();

    @Value("${myconfig.security.jwt.TEST_TOKEN}")
    private String tokenValue;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = System.getProperty("micro-url", "http://localhost:6666");
    }

    private void setUpBadToken() {
        final var badToken = "Bearer " + easyRandom.nextObject(String.class);
        headers.put("Authentication", badToken);
    }

    private void setUpBearerToken() {
        final var token = "Bearer " + tokenValue;
        headers.put("Authentication", token);
    }
    private void resetHeaders() {
        headers.clear();
    }

    @Test
    void delete_GivenBadToken_ThenReturnsUnauthorized() {
        setUpBadToken();
        final var authData = this.easyRandom.nextObject(AuthenticationData.class);

        RestAssured.given()
                .headers(headers)
                .body(authData)
                .when()
                .put(endpoint,"pathparam")
                .then()
                .assertThat()
                .statusCode(401);
        resetHeaders();
    }


    @Test
    void delete_GivenBearerToken_ThenReturns204() {
        setUpBearerToken();
        final var authData = this.easyRandom.nextObject(AuthenticationData.class);

        RestAssured.given()
                .headers(headers)
                .body(authData)
                .when()
                .put(endpoint,"pathparam")
                .then()
                .assertThat()
                .statusCode(204);
        resetHeaders();
    }

}
