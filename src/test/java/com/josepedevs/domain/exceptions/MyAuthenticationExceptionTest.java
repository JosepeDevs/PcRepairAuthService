package com.josepedevs.domain.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith( MockitoExtension.class )
class MyAuthenticationExceptionTest {

    @Test
    void getMyErrorMessage() {
        final MyAuthenticationException myAuthenticationException = new MyAuthenticationException("msg","attr");
        final var actual = myAuthenticationException.getMyErrorMessage();
        assertNotNull(actual);
    }

    @Test
    void getIllegalAttributeName() {
        final MyAuthenticationException myAuthenticationException = new MyAuthenticationException("msg","attr");
        final var actual = myAuthenticationException.getIllegalAttributeName();
        assertNotNull(actual);
    }
}