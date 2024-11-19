package com.josepedevs.domain.exceptions;

public class InadequateRoleException  extends MyRuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	//pasamos el mensaje a excepción padre y logeamos el error
	public InadequateRoleException(String myErrorMessage, String illegalAttributeName ) {
        super(myErrorMessage, InadequateRoleException.class.getName() );
    }
	public InadequateRoleException(String message) {
		super(message);
	}
}
