package com.bowlingsystem.bowlingsystem.util.exception;
/*
 * Defines structure for exception response
 */
public class ExceptionResponse {

	private String errorMessage;
    private String errorCode;
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}
