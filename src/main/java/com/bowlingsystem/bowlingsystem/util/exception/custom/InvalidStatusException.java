package com.bowlingsystem.bowlingsystem.util.exception.custom;

/*
 * Thrown when conflict in status of entities
 */
public class InvalidStatusException extends RuntimeException {

	public InvalidStatusException(String errorMessage) {
		super(errorMessage);
	}
}
