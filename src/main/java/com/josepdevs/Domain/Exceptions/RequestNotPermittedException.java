package com.josepdevs.Domain.Exceptions;

public class RequestNotPermittedException extends MyRuntimeException {

	private static final long serialVersionUID = 1L;
	
	public RequestNotPermittedException(String myErrorMessage, String illegalAttributeName) {
        super(myErrorMessage, RequestNotPermittedException.class.getName() );
    }
	
}
