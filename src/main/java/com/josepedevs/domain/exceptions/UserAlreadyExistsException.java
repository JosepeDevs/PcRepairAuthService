package com.josepedevs.domain.exceptions;

public class UserAlreadyExistsException  extends MyRuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	//pasamos el mensaje a excepción padre y logeamos el error
	public UserAlreadyExistsException(String myErrorMessage, String illegalAttributeName ) {
        super(myErrorMessage, UserAlreadyExistsException.class.getName() );
    }
	public UserAlreadyExistsException(String message) {
		super(message);
	}
}
