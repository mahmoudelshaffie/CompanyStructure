package org.tradeshit.companystructure.exceptions;

public class ResourceNotFoundException extends RuntimeException {
	
	private String code;
	
	public ResourceNotFoundException(String code) {
		this(code, null, null);
	}
	
	public ResourceNotFoundException(String code, String message) {
		this(code, message, null);
	}
	
	public ResourceNotFoundException(String code, Throwable cause) {
		this(code, null, cause);
	}
	
	public ResourceNotFoundException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	public String toJSON() {
		return "{code:'" + code + "',message:'" + getMessage() + "'}";
	}
}
