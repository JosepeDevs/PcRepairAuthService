package com.josepedevs.application.usecase.authenticationdata;

import com.josepedevs.domain.repository.AuthenticationDataRepository;
import com.josepedevs.domain.request.DeleteHardUserRequest;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteHardAuthenticationDataUseCaseImplTest {

    @InjectMocks
    private DeleteHardAuthenticationDataUseCaseImpl deleteHardAuthenticationDataUseCase;

    @Mock
    private AuthenticationDataRepository repository;

    private final EasyRandom easyRandom = new EasyRandom();

    @Test
    void apply_GivenTrueDeleteHardUserRequest_ThenReturnTrue() {

        // Arrange
        final var deleteHardUserRequest = this.easyRandom.nextObject(DeleteHardUserRequest.class);
        when(this.repository.deleteHard(deleteHardUserRequest.getUserId())).thenReturn(true);

        // Act
        final var actual = deleteHardAuthenticationDataUseCase.apply(deleteHardUserRequest);

        // Assert
        assertTrue(actual);

    }

    @Test
    void apply_GivenFalseDeleteHardUserRequest_ThenReturnFalse() {

        // Arrange
        final var deleteHardUserRequest = easyRandom.nextObject(DeleteHardUserRequest.class);
        when(this.repository.deleteHard(deleteHardUserRequest.getUserId())).thenReturn(false);

        // Act
        final var actual = deleteHardAuthenticationDataUseCase.apply(deleteHardUserRequest);

        // Assert
        assertFalse(actual);
    }
}