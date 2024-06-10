package com.josepdevs.Domain.Exceptions;

public class UserNotFoundException  extends MyRuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	//pasamos el mensaje a excepción padre y logeamos el error
	public UserNotFoundException(String myErrorMessage, String illegalAttributeName ) {
        super(myErrorMessage, UserNotFoundException.class.getName() );
    }
	
}
