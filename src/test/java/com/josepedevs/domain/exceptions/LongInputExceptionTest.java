package com.josepedevs.domain.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class LongInputExceptionTest {

    @Test
    void testException_GivenData_ConstructorWorksCorrectly() {
        final var exception = new LongInputException("msg");
        assertNotNull(exception);
    }
}