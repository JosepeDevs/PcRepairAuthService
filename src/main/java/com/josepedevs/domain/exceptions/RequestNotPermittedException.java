package com.josepedevs.domain.exceptions;

import java.io.Serial;

public class RequestNotPermittedException extends MyRuntimeException {

	@Serial
	private static final long serialVersionUID = 1L;
	
	public RequestNotPermittedException(String myErrorMessage) {
        super(myErrorMessage, RequestNotPermittedException.class.getName() );
    }
}
