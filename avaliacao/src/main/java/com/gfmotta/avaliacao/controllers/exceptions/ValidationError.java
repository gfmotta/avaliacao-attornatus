package com.gfmotta.avaliacao.controllers.exceptions;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError {
	private static final long serialVersionUID = 1L;
	
	private List<CustomFieldError> errors = new ArrayList<>();

	public List<CustomFieldError> getErrors() {
		return errors;
	}

	public void addError(String fieldName, String message) {
		errors.add(new CustomFieldError(fieldName, message));
	}
}
