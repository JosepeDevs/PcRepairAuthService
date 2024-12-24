package com.josepedevs.application.usecase.authenticationdata;

import com.josepedevs.domain.entity.AuthenticationData;
import com.josepedevs.domain.exceptions.UserNotFoundException;
import com.josepedevs.domain.repository.AuthenticationDataRepository;
import com.josepedevs.domain.request.PatchUserPasswordRequest;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatchAuthenticationDataPasswordUseCaseImplTest {

    @InjectMocks
    private PatchAuthenticationDataPasswordUseCaseImpl useCase;

    @Mock
    private AuthenticationDataRepository repository;

    @Mock
    private PasswordEncoder encoder;

    private final EasyRandom easyRandom = new EasyRandom();

    @Test
    void apply_GivenNotFoundUser_ThenThrowsUserNotFoundException() {
        final var request = this.easyRandom.nextObject(PatchUserPasswordRequest.class);

        when(this.repository.findById(request.getAuthDataId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            this.useCase.apply(request);
        });
    }

    @Test
    void apply_GivenNoChanges_ThenReturnsFalse() {
        final var request = this.easyRandom.nextObject(PatchUserPasswordRequest.class);
        final var authData = this.easyRandom.nextObject(AuthenticationData.class);
        final var encodedPassword = this.easyRandom.nextObject(String.class);

        when(this.encoder.encode(request.getNewPassword())).thenReturn(encodedPassword);
        when(this.repository.findById(request.getAuthDataId())).thenReturn(Optional.of(authData));
        when(this.repository.patchPassword(authData,encodedPassword)).thenReturn(false);

        final var actual = this.useCase.apply(request);

        assertFalse(actual);
    }

    @Test
    void apply_GivenChanges_ThenReturnsTrue() {
        final var request = this.easyRandom.nextObject(PatchUserPasswordRequest.class);
        final var authData = this.easyRandom.nextObject(AuthenticationData.class);
        final var encodedPassword = this.easyRandom.nextObject(String.class);

        when(this.encoder.encode(request.getNewPassword())).thenReturn(encodedPassword);
        when(this.repository.findById(request.getAuthDataId())).thenReturn(Optional.of(authData));
        when(this.repository.patchPassword(authData,encodedPassword)).thenReturn(true);

        final var actual = this.useCase.apply(request);

        assertTrue(actual);
    }
}