package com.josepdevs.Domain.Exceptions;

public class TokenNotValidException extends MyRuntimeException {

	private static final long serialVersionUID = 1L;
	
	public TokenNotValidException(String myErrorMessage, String illegalAttributeName) {
        super(myErrorMessage, TokenNotValidException.class.getName() );
    }
	
}
