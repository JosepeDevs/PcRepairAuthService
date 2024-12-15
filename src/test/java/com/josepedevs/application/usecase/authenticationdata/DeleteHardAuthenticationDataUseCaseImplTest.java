package com.josepedevs.application.usecase.authenticationdata;

import com.josepedevs.application.service.JwtMasterValidator;
import com.josepedevs.domain.exceptions.TokenNotValidException;
import com.josepedevs.domain.repository.AuthenticationDataRepository;
import com.josepedevs.domain.request.DeleteHardUserRequest;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteHardAuthenticationDataUseCaseImplTest {

    @InjectMocks
    private DeleteHardAuthenticationDataUseCaseImpl deleteHardAuthenticationDataUseCase;

    @Mock
    private AuthenticationDataRepository repository;

    @Mock
    private JwtMasterValidator validator;

    private final EasyRandom easyRandom = new EasyRandom();

    @Test
    void apply_GivenAdminAndDeleteHardUserRequest_ThenReturnsTrue() {

        // Arrange
        final var deleteHardUserRequest = this.easyRandom.nextObject(DeleteHardUserRequest.class);
        when(this.repository.deleteHard(deleteHardUserRequest.getUserId())).thenReturn(true);
        when(this.validator.isAdminTokenCompletelyValidated(deleteHardUserRequest.getJwtToken())).thenReturn(true);

        // Act
        final var actual = deleteHardAuthenticationDataUseCase.apply(deleteHardUserRequest);

        // Assert
        assertTrue(actual);

    }
    @Test
    void apply_GivenNonAdminAndDeleteHardUserRequest_ThenThrowsException() {

        // Arrange
        final var deleteHardUserRequest = this.easyRandom.nextObject(DeleteHardUserRequest.class);
        when(this.validator.isAdminTokenCompletelyValidated(deleteHardUserRequest.getJwtToken())).thenReturn(false);

        // Act and assert
        assertThrows(TokenNotValidException.class,
                () -> deleteHardAuthenticationDataUseCase.apply(deleteHardUserRequest)
        );

    }

    @Test
    void apply_GivenFalseDeleteHardUserRequest_ThenReturnFalse() {

        // Arrange
        final var deleteHardUserRequest = easyRandom.nextObject(DeleteHardUserRequest.class);
        when(this.repository.deleteHard(deleteHardUserRequest.getUserId())).thenReturn(false);
        when(this.validator.isAdminTokenCompletelyValidated(deleteHardUserRequest.getJwtToken())).thenReturn(true);

        // Act
        final var actual = deleteHardAuthenticationDataUseCase.apply(deleteHardUserRequest);

        // Assert
        assertFalse(actual);
    }
}