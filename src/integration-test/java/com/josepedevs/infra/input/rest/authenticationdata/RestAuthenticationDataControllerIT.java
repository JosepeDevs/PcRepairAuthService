package com.josepedevs.infra.input.rest.authenticationdata;

import com.josepedevs.domain.entity.AuthenticationData;
import io.restassured.RestAssured;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@ActiveProfiles("test")
class RestAuthenticationDataControllerIT {

    private static final String NO_AUTH_ENDPOINT = "/api/v1/noauth/authenticate";
    private static final String ADMIN_NDPOINT = "/api/v1/admin/getall";
    private static final String SECURED_NDPOINT = "/api/v1/newpassword";

    private Map<String,String> headers = new HashMap<>();

    private final EasyRandom easyRandom = new EasyRandom();

    private final String tokenValue = "98b4e7a3-6747-41f4-9f90-bd2ac196e4d2";

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = System.getProperty("micro-url", "http://localhost:6666");
    }


    @Test
    void delete_GivenNoToken_ThenReturnsAuthorized() {
        setUpBadToken();
        final var authData = this.easyRandom.nextObject(AuthenticationData.class);

        RestAssured.given()
                .headers(headers)
                .body(authData)
                .when()
                .post(NO_AUTH_ENDPOINT)
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
                .put(NO_AUTH_ENDPOINT,"pathparam")
                .then()
                .assertThat()
                .statusCode(204);
        resetHeaders();
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
}
