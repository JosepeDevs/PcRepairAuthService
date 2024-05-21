package com.josepdevs.Domain.Exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.log4j.Log4j2;

@ControllerAdvice //This annotation applies the exception handler across the whole application = global exception handler. This class can contain multiple methods, each annotated with @ExceptionHandler, to handle different types of exceptions.
@Log4j2
public class GlobalExceptionHandler {
	
	//CENTRALIZED EXCEPTION MANAGER, built-in and personalized exceptions
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Map<String, String>> myOwnHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        //create the map that will contain the errorName and the message 
		Map<String, String> errorDetails = new HashMap<>();
		//put in the map this generic message, for each exception we will personalize it
		errorDetails.put("Http Error", "Serialization of the received JSON body failed, maybe some validation of the valueObjects failed.");
        //return in the body the error
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordNotValidException.class)
    public ResponseEntity<Map<String, String>> handlePasswordNotValidError(PasswordNotValidException ex) {
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("password", "Password did not meet either the required lower case, upper case and symbol requirement or the length was not enough");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); 
        return new ResponseEntity<>(errorDetails, headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("error", "An illegal argument was provided.");
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = NullPointerException.class)
    public ResponseEntity<Map<String, String>> handleNullPointerException(NullPointerException ex) {
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("error", "A null pointer exception occurred.");
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    
}