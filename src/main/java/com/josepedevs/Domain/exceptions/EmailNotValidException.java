package com.josepedevs.Domain.exceptions;

public class EmailNotValidException extends MyRuntimeException {

	private static final long serialVersionUID = 1L;
	
	public EmailNotValidException(String myErrorMessage, String illegalAttributeName) {
        super(myErrorMessage, EmailNotValidException.class.getName() );
    }
	public EmailNotValidException(String message) {
		super(message);
	}
}
