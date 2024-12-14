package com.josepedevs.domain.exceptions;

import java.io.Serial;

public class PasswordNotValidException extends MyRuntimeException {

	@Serial
	private static final long serialVersionUID = 1L;

	public PasswordNotValidException(String myErrorMessage) {
        super(myErrorMessage, PasswordNotValidException.class.getName());
    }

}