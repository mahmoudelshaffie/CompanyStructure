package org.tradeshit.companystructure.exceptions.handlers;


import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.tradeshit.companystructure.exceptions.ResourceNotFoundException;

@ControllerAdvice
public class GeneralExceptionHandler {
	
	private Logger logger = LoggerFactory.getLogger(GeneralExceptionHandler.class);
	
	private void log(Exception ex) {
		StringWriter strWriter = new StringWriter(1000);
		PrintWriter writer = new PrintWriter(strWriter);
		ex.printStackTrace(writer);
		
		logger.error(strWriter.toString());
	}
	
	@ResponseBody
	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleIllegalArgumentException(ResourceNotFoundException ex) {
		log(ex);
		return ex.toJSON();
	}
	
	@ResponseBody
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleIGeneralException(Exception ex) {
		log(ex);
		return "Internal Server Error";
	}
}
