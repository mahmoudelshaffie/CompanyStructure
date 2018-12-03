package org.tradeshit.companystructure.exceptions;

public class NoInitializedStructureException extends ResourceNotFoundException {

	public NoInitializedStructureException() {
		super("NO_INITIALIZED_STRUCTURE", "Company structure not initialized yet, you've to initialize it by creating its root !!!");
	}
	
}
