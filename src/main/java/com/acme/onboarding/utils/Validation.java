package com.acme.onboarding.utils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;

import java.util.Iterator;
import java.util.Set;


public class Validation {

    public static <T> void checkValid(Validator validator,T object){
        Set<ConstraintViolation<T>> validations = validator.validate(object);
        if(!validations.isEmpty()){
            ConstraintViolation<T> violation = validations.iterator().next();
            String message = getFieldName(violation.getPropertyPath()) + " " + violation.getMessage();
            throw new ValidationException(message);
        }
    }
    private static String getFieldName(Path propertyPath) {
        // Traverse the path to get the last node, which represents the field name
        Iterator<Path.Node> iterator = propertyPath.iterator();
        String fieldName = null;
        while (iterator.hasNext()) {
            fieldName = iterator.next().getName();
        }
        return fieldName;
    }
}
