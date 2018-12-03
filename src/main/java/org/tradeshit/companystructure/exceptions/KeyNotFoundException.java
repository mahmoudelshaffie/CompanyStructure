package org.tradeshit.companystructure.exceptions;

public class KeyNotFoundException extends ResourceNotFoundException {
	public KeyNotFoundException(String key) {
		super(KEY, String.format(MESSAGE, key));
	}

	public static String KEY = "KEY_NOT_FOUND";
	public static String MESSAGE = "Key: %1$s not found";
}
