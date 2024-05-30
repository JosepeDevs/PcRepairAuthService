package com.josepdevs.Domain.Exceptions;

public class InadequateRoleException  extends MyRuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	//pasamos el mensaje a excepción padre y logeamos el error
	public InadequateRoleException(String myErrorMessage, String illegalAttributeValue ) {
        super(myErrorMessage, illegalAttributeValue );
    }
	
}
