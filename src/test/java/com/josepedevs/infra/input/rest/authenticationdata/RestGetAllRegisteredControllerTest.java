package com.josepedevs.infra.input.rest.authenticationdata;

import com.josepedevs.application.usecase.authenticationdata.GetAllAuthenticationDataUseCaseImpl;
import com.josepedevs.domain.entity.AuthenticationData;
import com.josepedevs.domain.exceptions.RequestNotPermittedException;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RestGetAllRegisteredControllerTest {

    @InjectMocks
    private RestGetAllRegisteredController controller;

    @Mock
    private GetAllAuthenticationDataUseCaseImpl useCase;

    private final EasyRandom easyRandom = new EasyRandom();

    @Test
    void getAllRegistered_ShouldReturnAListAndResponseStatusOk() {
        final var token = "Bearer token";
        final var tokenValue = "token";
        final var auth1 = this.easyRandom.nextObject(AuthenticationData.class).toBuilder().role("ADMIN").currentToken(tokenValue).build();
        final var auth2 = this.easyRandom.nextObject(AuthenticationData.class).toBuilder().role("ADMIN").active(false).build();
        final var auth3 = this.easyRandom.nextObject(AuthenticationData.class).toBuilder().role("EDITOR").active(true).build();
        final var mockDataList = List.of(auth1, auth2, auth3);

        when(this.useCase.apply(auth1.getCurrentToken())).thenReturn(mockDataList);

        final var result = this.controller.getAllRegistered(token);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(mockDataList);
    }

    @Test
    void getFallbackRegisteredBulkHead_ShouldReturnErrorMessageAndResponseStatusTooManyRequest429() {
        final var exception = new RequestNotPermittedException("Texto de error");

        final var result = this.controller.getFallbackRegisteredBulkHead(exception);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.TOO_MANY_REQUESTS);
        assertNotNull(result.getBody());
    }
}