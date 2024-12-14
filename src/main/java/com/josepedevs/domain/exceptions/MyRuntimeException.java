package com.josepedevs.domain.exceptions;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.io.Serial;

@Log4j2
@Getter
public class MyRuntimeException extends RuntimeException {
	
	@Serial
	private static final long serialVersionUID = 1L;
	private final String myErrorMessage;
	private final String illegalAttributeName;

	public MyRuntimeException(String myErrorMessage, String illegalAttributeName) {
		super(myErrorMessage);
        this.myErrorMessage = myErrorMessage;
        this.illegalAttributeName = illegalAttributeName;
		log.error(myErrorMessage,illegalAttributeName);
	}

}
