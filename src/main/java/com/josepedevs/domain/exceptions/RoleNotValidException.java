package com.josepedevs.domain.exceptions;

import java.io.Serial;

public class RoleNotValidException extends MyRuntimeException{
	
	@Serial
	private static final long serialVersionUID = 1L;
	
	public RoleNotValidException(String myErrorMessage) {
        super(myErrorMessage, RoleNotValidException.class.getName());
    }
}
