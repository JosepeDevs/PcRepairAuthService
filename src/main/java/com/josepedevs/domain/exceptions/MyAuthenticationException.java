package com.josepedevs.domain.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;

@Slf4j
public class MyAuthenticationException extends AuthenticationException {

    public MyAuthenticationException(String message) {
        super(message);
    }

    private static final long serialVersionUID = 1L;
    private String myErrorMessage;
    private String illegalAttributeName;

    //pasamos el mensaje a excepción padre y logeamos el error
    public MyAuthenticationException(String myErrorMessage, String illegalAttributeName) {
        super(myErrorMessage);
        this.illegalAttributeName = illegalAttributeName;
        this.myErrorMessage = myErrorMessage;

        log.error(myErrorMessage, illegalAttributeName);
    }

    public String getMyErrorMessage() {
        return this.myErrorMessage;
    }

    public String getIllegalAttributeName() {
        return this.illegalAttributeName;
    }
}

