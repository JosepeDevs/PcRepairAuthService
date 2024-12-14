package com.josepedevs.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.NoSuchElementException;


@ControllerAdvice
public class GlobalExceptionHandler {

    //THIS MUST BE UNCOMMENTED WHEN UPLOADING TO PROD (we do not want to expose our app's info to external clients)
    //COMMENT THIS EXCEPTION WHEN DEBUGGING TO KNOW EXACT EXCEPTION
    @ExceptionHandler(Exception.class)
    public ResponseEntity<HashMap<Object, Object>> globalExceptionManager(Exception ex) {
        final var errorDetails = new HashMap<>();
        errorDetails.put("Exception", "A problem happened.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }

    @ExceptionHandler(MyRuntimeException.class)
    public ResponseEntity<HashMap<Object, Object>> handleMyRunTimeExceptions(MyRuntimeException ex) {
        final var errorDetails = new HashMap<>();
        errorDetails.put("The attribute with name: '"+ex.getIllegalAttributeName()+"' was not valid", ex.getMyErrorMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HashMap<Object, Object>> myBadCredentialsException(BadCredentialsException ex) {
        final var errorDetails = new HashMap<>();
        errorDetails.put("Credentials", "Review your credentials as they could not be validated or were missing");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDetails);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<HashMap<Object, Object>> myNoResourceFoundException(NoResourceFoundException ex) {
        final var errorDetails = new HashMap<>();
        errorDetails.put("NoResourceFoundException", "The resource you were looking for does not exist or has been removed. Maybe that endpoints does not exists..");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<HashMap<Object, Object>> myNoSuchElementException(NoSuchElementException ex) {
        final var errorDetails = new HashMap<>();
        errorDetails.put("ThatElementWasNotFound", "Upon accessing the element we found it empty/missing.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<HashMap<Object, Object>> myOwnHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        final var errorDetails = new HashMap<>();
        errorDetails.put("Http Error", "A problem with the HTTP message occurred and cannot be read properly, maybe it could not be parsed correctly to/from JSON.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }

    @ExceptionHandler(ClassCastException.class)
    public ResponseEntity<HashMap<Object, Object>> myClassCastException(ClassCastException ex) {
        final var errorDetails = new HashMap<>();
        errorDetails.put("ClassProblem", "A variable was tried to be saved as a certain Class, however that operation was not possible (ClassCastException).");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<HashMap<Object, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        final var errorDetails = new HashMap<>();
        errorDetails.put("Illegal argument error", "The argument/parameter that was in use/used was not expected (illegal argument).");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }

    @ExceptionHandler(value = NullPointerException.class)
    public ResponseEntity<HashMap<Object, Object>> handleNullPointerException(NullPointerException ex) {
        final var errorDetails = new HashMap<>();
        errorDetails.put("Null resource error", "You tried to use a null resource");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }

}