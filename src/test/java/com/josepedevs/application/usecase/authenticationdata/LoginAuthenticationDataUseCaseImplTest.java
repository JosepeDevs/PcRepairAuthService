package com.josepedevs.application.usecase.authenticationdata;

import com.josepedevs.application.service.GetUserFromTokenUsernameService;
import com.josepedevs.application.service.JwtTokenIssuerService;
import com.josepedevs.domain.entity.AuthenticationData;
import com.josepedevs.domain.exceptions.MyAuthenticationException;
import com.josepedevs.domain.exceptions.TokenNotValidException;
import com.josepedevs.domain.repository.AuthenticationDataRepository;
import com.josepedevs.domain.request.AuthenticationRequest;
import com.josepedevs.domain.request.AuthenticationResponse;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginAuthenticationDataUseCaseImplTest {

        @InjectMocks
        private LoginAuthenticationDataUseCaseImpl useCase;

        @Mock
        private AuthenticationDataRepository repository;

        @Mock
        private GetUserFromTokenUsernameService getUserService;

        @Mock
        private AuthenticationManager authenticationManager;

        @Mock
        private JwtTokenIssuerService jwtService;

        private final EasyRandom easyRandom = new EasyRandom();

        @Test
        void login_GivenValidToken_ThenReturnsAuthenticationResponse (){
            // Arrange
            final var authData = this.easyRandom.nextObject(AuthenticationData.class);
            final var authRequest = AuthenticationRequest
                    .builder()
                    .username(authData.getUsername())
                    .psswrd(authData.getPsswrd())
                    .build();
            final var authResponse = AuthenticationResponse
                    .builder()
                    .token(authData.getCurrentToken())
                    .build();

            when(getUserService.getUserFromTokenUsername(authRequest.getUsername())).thenReturn(authData);
            when(jwtService.generateToken(any(HashMap.class), eq(authData))).thenReturn(authData.getCurrentToken());
            when(repository.login(authData, authData.getCurrentToken())).thenReturn(true);

            // Act
            final var actual = useCase.apply(authRequest);

            // Assert
            assertEquals(authResponse, actual);
            verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(authData.getUsername(), authData.getPsswrd()));
        }


    @Test
    void login_GivenInvalidAuthentication_ThenThrowsTokenNotvalidException() {
        // Arrange
        final var authData = this.easyRandom.nextObject(AuthenticationData.class);

        when(getUserService.getUserFromTokenUsername(authData.getUsername())).thenReturn(authData);
        when(jwtService.generateToken(any(HashMap.class), eq(authData))).thenReturn(authData.getCurrentToken());
        when(repository.login(authData, authData.getCurrentToken())).thenReturn(false);

        // Act & Assert
        assertThrows(TokenNotValidException.class, () -> {
            useCase.apply(
                    new AuthenticationRequest(authData.getUsername(),authData.getPassword())
            );
        });

        verify(authenticationManager).authenticate(any());
    }

    @Test
    void login_GivenInvalidAuthentication_ThenThrowsAuthenticationException() {
        // Arrange
        final var authData = this.easyRandom.nextObject(AuthenticationData.class);

        when(authenticationManager.authenticate(any()))
                .thenThrow(new MyAuthenticationException("Invalid credentials"));

        // Act & Assert
        assertThrows(AuthenticationException.class, () -> {
            useCase.apply(
                    new AuthenticationRequest(authData.getUsername(),authData.getPassword())
            );
        });

        verify(authenticationManager).authenticate(any());
    }

}