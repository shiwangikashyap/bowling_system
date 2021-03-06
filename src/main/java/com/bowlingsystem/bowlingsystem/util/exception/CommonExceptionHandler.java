package com.bowlingsystem.bowlingsystem.util.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bowlingsystem.bowlingsystem.util.exception.custom.EntityNotFoundException;
import com.bowlingsystem.bowlingsystem.util.exception.custom.InvalidStatusException;

@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    /*
     * Thrown with status code 404 when entity in db does not exist
     */
    public ResponseEntity<ExceptionResponse> resourceNotFound(EntityNotFoundException ex) {
        ExceptionResponse response = new ExceptionResponse();
        response.setErrorCode("NOT_FOUND");
        response.setErrorMessage(ex.getMessage());
        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidStatusException.class)
    /*
     * Thrown with status code 409 when conflict in status of entities
     */
    public ResponseEntity<ExceptionResponse> resourceAlreadyExists(InvalidStatusException ex) {
        ExceptionResponse response=new ExceptionResponse();
        response.setErrorCode("CONFLICT");
        response.setErrorMessage(ex.getMessage());
        return new ResponseEntity<ExceptionResponse>(response, HttpStatus.CONFLICT);
    }
}