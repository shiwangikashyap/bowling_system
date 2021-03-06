package com.bowlingsystem.bowlingsystem.util.exception.custom;

@SuppressWarnings("serial")
public class EntityNotFoundException extends RuntimeException{

    /*
     * Thrown when entity in db does not exist
     */
	public EntityNotFoundException(String errorMessage) {
		super(errorMessage);
	}
}
