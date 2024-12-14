package com.josepedevs.domain.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.NoSuchElementException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    void globalExceptionManager_GivenException_ThenResponseBodyContainsMapWithException() {
        // Arrange
        final var exception = new Exception();
        // Act
        final var actual = globalExceptionHandler.globalExceptionManager(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
        assertFalse(Objects.requireNonNull(actual.getBody()).toString().isBlank(), "There is a message explaining the error");
    }

    @Test
    void handleMyRunTimeExceptions_GivenException_ThenResponseBodyContainsMapWithException() {
        // Arrange
        final var exception = new MyRuntimeException("attribute","what went wrong");
        // Act
        final var actual = globalExceptionHandler.handleMyRunTimeExceptions(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
        assertFalse(Objects.requireNonNull(actual.getBody()).toString().isBlank(), "There is a message explaining the error");
    }

    @Test
    void myBadCredentialsException_GivenBadCredentialsException_ThenResponseBodyContainsMapWithException() {
        // Arrange
        final var exception = new BadCredentialsException("msg");
        // Act
        final var actual = globalExceptionHandler.myBadCredentialsException(exception);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, actual.getStatusCode());
        assertFalse(Objects.requireNonNull(actual.getBody()).toString().isBlank(), "There is a message explaining the error");
    }

    @Test
    void myNoResourceFoundException_GivenNoResourceFoundException_ThenResponseBodyContainsMapWithException() {
        // Arrange
        final var exception = new NoResourceFoundException(HttpMethod.GET,"path");
        // Act
        final var actual = globalExceptionHandler.myNoResourceFoundException(exception);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
        assertFalse(Objects.requireNonNull(actual.getBody()).toString().isBlank(), "There is a message explaining the error");
    }

    @Test
    void myNoSuchElementException_GivenNoSuchElementException_ThenResponseBodyContainsMapWithException() {
        // Arrange
        final var exception = new NoSuchElementException();
        // Act
        final var actual = globalExceptionHandler.myNoSuchElementException(exception);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
        assertFalse(Objects.requireNonNull(actual.getBody()).toString().isBlank(), "There is a message explaining the error");
    }

    @Test
    void myOwnHttpMessageNotReadableException_GivenException_ThenResponseBodyContainsMapWithException() {
        // Arrange
        final var servletRequest = new MockHttpServletRequest();
        final var exception = new HttpMessageNotReadableException("msg", new ServletServerHttpRequest(servletRequest));
        // Act
        final var actual = globalExceptionHandler.myOwnHttpMessageNotReadableException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
        assertFalse(Objects.requireNonNull(actual.getBody()).toString().isBlank(), "There is a message explaining the error");
    }

    @Test
    void myClassCastException_GivenException_ThenResponseBodyContainsMapWithException() {
        // Arrange
        final var exception = new ClassCastException();
        // Act
        final var actual = globalExceptionHandler.myClassCastException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
        assertFalse(Objects.requireNonNull(actual.getBody()).toString().isBlank(), "There is a message explaining the error");
    }

    @Test
    void handleIllegalArgumentException_GivenException_ThenResponseBodyContainsMapWithException() {
        // Arrange
        final var exception = new IllegalArgumentException();
        // Act
        final var actual = globalExceptionHandler.handleIllegalArgumentException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
        assertFalse(Objects.requireNonNull(actual.getBody()).toString().isBlank(), "There is a message explaining the error");
    }

    @Test
    void handleNullPointerException_GivenException_ThenResponseBodyContainsMapWithException() {
        // Arrange
        final var exception = new NullPointerException();
        // Act
        final var actual = globalExceptionHandler.handleNullPointerException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
        assertFalse(Objects.requireNonNull(actual.getBody()).toString().isBlank(), "There is a message explaining the error");
    }
}