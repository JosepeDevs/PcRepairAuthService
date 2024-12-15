package com.josepedevs.application.service;

import com.josepedevs.domain.entity.AuthenticationData;
import com.josepedevs.domain.exceptions.UserNotFoundException;
import com.josepedevs.domain.repository.AuthenticationDataRepository;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthDataFinderTest {

    @InjectMocks
    private AuthDataFinder service;

    @Mock
    private AuthenticationDataRepository repository;

    private final EasyRandom easyRandom = new EasyRandom();

    @Test
    void findById_GivenFoundOpt_ThenReturnsAuthData() {
        // Arrange
        final var authData = this.easyRandom.nextObject(AuthenticationData.class);
        when(repository.findById(authData.getIdUser())).thenReturn(Optional.of(authData));
        // Act
        final var actual = this.service.findById(authData.getIdUser());
        // Assert
        assertEquals(authData, actual);
    }
    @Test
    void findById_GivenEmptyOpt_ThenThrowsUserNotFoundException() {
        // Arrange
        final var authData = this.easyRandom.nextObject(AuthenticationData.class);
        when(repository.findById(authData.getIdUser())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            service.findById(authData.getIdUser());
        });
    }

    @Test
    void findByUserName_GivenFoundOpt_ThenReturnsAuthData() {
        // Arrange
        final var authData = this.easyRandom.nextObject(AuthenticationData.class);
        when(repository.findByUsername(authData.getUsername())).thenReturn(Optional.of(authData));
        // Act
        final var actual = this.service.findByUsername(authData.getUsername());
        // Assert
        assertEquals(authData, actual);
    }
    @Test
    void findByUserName_GivenEmptyOpt_ThenThrowsUserNotFoundException() {
        // Arrange
        final var authData = this.easyRandom.nextObject(AuthenticationData.class);
        when(repository.findByUsername(authData.getUsername())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            service.findByUsername(authData.getUsername());
        });
    }
}