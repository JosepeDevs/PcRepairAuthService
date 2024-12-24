package com.josepedevs.application.usecase.authenticationdata;

import com.josepedevs.application.service.JwtIssuerService;
import com.josepedevs.domain.entity.AuthenticationData;
import com.josepedevs.domain.exceptions.UserAlreadyExistsException;
import com.josepedevs.domain.repository.AuthenticationDataRepository;
import com.josepedevs.domain.request.RegisterRequest;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterAuthenticationDataUseCaseImplTest {

    @InjectMocks
    private RegisterAuthenticationDataUseCaseImpl useCase;
    @Mock
    private AuthenticationDataRepository repository;
    @Mock
    private PasswordEncoder encoder;
    @Mock
    private JwtIssuerService jwtService;
    private final EasyRandom easyRandom = new EasyRandom();

    @Test
    void apply_GivenAlreadyExistingUserName_ThenThrowsUserAlreadyExistsException() {
        final var request = this.easyRandom.nextObject(RegisterRequest.class);

        when(this.repository.findByUsername(request.getUsername())).thenReturn(Optional.of(this.easyRandom.nextObject(AuthenticationData.class)));

        assertThrows(UserAlreadyExistsException.class, () -> this.useCase.apply(request));
    }

    @Test
    void apply_GivenAlreadyExistingEmail_ThenThrowsUserAlreadyExistsException() {
        final var request = this.easyRandom.nextObject(RegisterRequest.class);

        when(this.repository.findByEmail(request.getEmail())).thenReturn(Optional.of(this.easyRandom.nextObject(AuthenticationData.class)));

        assertThrows(UserAlreadyExistsException.class, () -> this.useCase.apply(request));
    }

    @Test
    void apply_GivenNotAlreadyRegisteredUser_ThenReturnsAuthDataId() {
        final var request = this.easyRandom.nextObject(RegisterRequest.class);
        final var defaultAuthData = this.easyRandom.nextObject(AuthenticationData.class)
                .toBuilder()
                .idUser(null)
                .username(request.getUsername())
                .email(request.getEmail())
                .psswrd(request.getPsswrd())
                .currentToken(null)
                .active(true)
                .role("USER")
                .build();

        when(this.repository.findByUsername(request.getUsername())).thenReturn(Optional.empty());
        when(this.repository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(this.encoder.encode(request.getPsswrd())).thenReturn(defaultAuthData.getPsswrd());
        when(this.jwtService.generateBasicToken(defaultAuthData)).thenReturn(defaultAuthData.getCurrentToken());
        when(this.repository.registerUserAuthData(defaultAuthData, defaultAuthData.getCurrentToken())).thenReturn(defaultAuthData);

        final var actual = this.useCase.apply(request);

        assertEquals(defaultAuthData.getIdUser(), actual);
    }
}