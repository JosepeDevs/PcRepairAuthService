package com.josepdevs.Domain.Exceptions;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice //This annotation applies the exception handler across the whole application = global exception handler. This class can contain multiple methods, each annotated with @ExceptionHandler, to handle different types of exceptions.
public class GlobalExceptionHandler {
	
	//MY CUSTOMIZED EXCEPTIONS
	
    @ExceptionHandler(MyRuntimeException.class) 
    public ResponseEntity handleMyExceptions(MyRuntimeException ex) {
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("The attribute with name: '"+ex.getIllegalAttributeName()+"' was not valid", ex.getMyErrorMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }
    
    
    //////////BUILT-IN EXCEPTIONS HANDLING TO NOW SHOW STACKTRACE
    
    //UNCOMMENT THIS WHEN UPLOADING TO PROD (we do not want to expose info to external clients)
    
    //COMMENT THIS EXCEPTION WHEN DEBUGGING TO KNOW EXACT EXCEPTION
	@ExceptionHandler(Exception.class)
	public ResponseEntity globalExceptionManager(Exception ex) {
		Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("Exception", "A problem happened.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }
	
    
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity myBadCredentialsException(BadCredentialsException ex) {
		Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("Credentials", "Review your credentials as they could not be validated or were missing");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDetails);
    }
	
	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity myNoResourceFoundException(NoResourceFoundException ex) {
		Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("NoResourceFoundException", "The resouce you were looking for does not exist or has been removed. Maybe that endpoints does not exists..");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
    }
	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity myNoSuchElementException(NoSuchElementException ex) {
		Map<String, String> errorDetails = new HashMap<>();
		errorDetails.put("ThatElementWasNotFound", "Upon accessing the element we found it empty/missing.");
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
	}
        
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity myOwnHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
		Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("Http Error", "A problem with the HTTP message occurred and cannot be read properly, maybe it could not be parsed correctly to/from JSON.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }
	@ExceptionHandler(ClassCastException.class)
	public ResponseEntity myClassCastException(ClassCastException ex) {
		Map<String, String> errorDetails = new HashMap<>();
		errorDetails.put("ClassProblem", "A variable was tried to be saved as a certain Class, however that operation was not possible (ClassCastException).");
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