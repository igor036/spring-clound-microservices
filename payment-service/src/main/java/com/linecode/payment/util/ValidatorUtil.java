package com.linecode.payment.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import com.linecode.payment.exception.UnprocessableEntityException;
import com.linecode.payment.exception.RestException;

import org.springframework.http.HttpStatus;

public class ValidatorUtil {
    
    private static final Validator validator =  Validation.buildDefaultValidatorFactory().getValidator();

    private ValidatorUtil() {
        // only static methods
    }

    public static void assertNotNull(Object object, String message) {
        if (object == null) {
            throw new RestException(HttpStatus.UNPROCESSABLE_ENTITY, message);
        }
    }

    public static <T> void assertConstraints(List<T> list) { 
        list.forEach(ValidatorUtil::assertConstraints);
    }

    public static <T> void assertConstraints(T object) {
		var violations = validator.validate(object);
		if (!violations.isEmpty()) {
            var errors = getPropertyErrorMessageMap(violations);
            throw new UnprocessableEntityException(errors);
		}
    }
    
    private static <T> Map<String, String> getPropertyErrorMessageMap(Set<ConstraintViolation<T>> violations) {
        var violationsMap = new HashMap<String, String>();
        violations.stream().forEach(violation -> {
            var property = violation.getPropertyPath().toString();
            var message = violation.getMessage();
            violationsMap.put(property, message);
        });
        return violationsMap;
    }
}
