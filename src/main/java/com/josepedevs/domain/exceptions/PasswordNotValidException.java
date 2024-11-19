package com.josepedevs.domain.exceptions;

public class PasswordNotValidException extends MyRuntimeException {

	private static final long serialVersionUID = 1L;

	public PasswordNotValidException(String myErrorMessage, String illegalAttributeName ) {
        super(myErrorMessage, PasswordNotValidException.class.getName() );
    }

	public PasswordNotValidException(String message) {
			super(message);
	}
	
}