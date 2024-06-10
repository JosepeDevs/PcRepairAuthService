package com.josepdevs.Domain.Exceptions;

public class BusyOrDownServerException extends MyRuntimeException {

	private static final long serialVersionUID = 1L;
	
	public BusyOrDownServerException(String myErrorMessage, String illegalAttributeName) {
        super(myErrorMessage, BusyOrDownServerException.class.getName() );
    }
	
}
