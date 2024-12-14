package com.josepedevs.domain.exceptions;

import java.io.Serial;

public class TokenNotValidException extends MyRuntimeException {

	@Serial
	private static final long serialVersionUID = 1L;
	
	public TokenNotValidException(String myErrorMessage) {
        super(myErrorMessage, TokenNotValidException.class.getName() );
    }
}
