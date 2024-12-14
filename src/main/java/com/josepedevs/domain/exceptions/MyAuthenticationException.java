package com.josepedevs.domain.exceptions;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;

import java.io.Serial;

@Slf4j
@Getter
public class MyAuthenticationException extends AuthenticationException {

    @Serial
    private static final long serialVersionUID = 1L;
    private String myErrorMessage;
    private String illegalAttributeName;

    public MyAuthenticationException(String myErrorMessage, String illegalAttributeName) {
        super(myErrorMessage);
        this.illegalAttributeName = illegalAttributeName;
        this.myErrorMessage = myErrorMessage;
        log.error(myErrorMessage, illegalAttributeName);
    }

}

