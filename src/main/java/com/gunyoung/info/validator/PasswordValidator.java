package com.gunyoung.info.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value== null)
			return false;
		
		return value.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$"); // Minimum eight characters, at least one letter and one number 
	}

}
