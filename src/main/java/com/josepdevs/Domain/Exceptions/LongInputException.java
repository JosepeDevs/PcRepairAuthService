package com.josepdevs.Domain.Exceptions;

public class LongInputException extends MyRuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	//pasamos el mensaje a excepción padre y logeamos el error
	public LongInputException(String myErrorMessage, String illegalAttributeName) {
        super(myErrorMessage, LongInputException.class.getName());
    }

}
