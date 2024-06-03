package com.josepdevs.Domain.Exceptions;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class MyRuntimeException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	private String myErrorMessage;
	private String illegalAttributeName;
	
	//pasamos el mensaje a excepción padre y logeamos el error
	public MyRuntimeException(String myErrorMessage, String illegalAttributeName) {
        super(myErrorMessage);
        this.illegalAttributeName = illegalAttributeName;
        this.myErrorMessage = myErrorMessage;
        
        log.error(myErrorMessage,illegalAttributeName);
    }
	
	public MyRuntimeException(String message) {
		super(message);
	}
	
	public String getMyErrorMessage() {
		return this.myErrorMessage;
	}
	public String getIllegalAttributeName() {
		return this.illegalAttributeName;
	}
	
}
