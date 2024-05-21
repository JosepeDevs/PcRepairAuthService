package com.josepdevs.Domain.Exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice //This annotation applies the exception handler across the whole application = global exception handler. This class can contain multiple methods, each annotated with @ExceptionHandler, to handle different types of exceptions.
public class GlobalExceptionHandler {
	
	//MY CUSTOMIZED EXCEPTIONS
	
    @ExceptionHandler(MyRuntimeException.class)
    public ResponseEntity handleMyExceptions(MyRuntimeException ex) {
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("The attribute with value: '"+ex.getIllegalAttributeValue()+"' was not valid", ex.getMyErrorMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }
    
    
    //////////BUILT-IN EXCEPTIONS HANDLING TO NOW SHOW STACKTRACE
    
    
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity myOwnHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
		Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("Http Error", "A problem with the HTTP message occurred and cannot be read properly, maybe it could not be parsed correctly to/from JSON.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }
    
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("Illegal argument error", "The argument/parameter that was in use/used was not expected (illegal argument).");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }

    @ExceptionHandler(value = NullPointerException.class)
    public ResponseEntity handleNullPointerException(NullPointerException ex) {
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("Null resource error", "You tried to use a null resource");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }
    
    
}