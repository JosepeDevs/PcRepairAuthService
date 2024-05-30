package com.josepdevs.Domain.Exceptions;

public class UserAlreadyExistsException  extends MyRuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	//pasamos el mensaje a excepción padre y logeamos el error
	public UserAlreadyExistsException(String myErrorMessage, String illegalAttributeValue ) {
        super(myErrorMessage, illegalAttributeValue );
    }
	
}
