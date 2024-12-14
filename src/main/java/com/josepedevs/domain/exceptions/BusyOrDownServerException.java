package com.josepedevs.domain.exceptions;

import lombok.extern.slf4j.Slf4j;

import java.io.Serial;

@Slf4j
public class BusyOrDownServerException extends MyRuntimeException {

	@Serial
	private static final long serialVersionUID = 1L;
	
	public BusyOrDownServerException(String myErrorMessage) {
        super(myErrorMessage, BusyOrDownServerException.class.getName());
	}
}
