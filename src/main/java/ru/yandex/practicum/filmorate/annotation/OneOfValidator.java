package ru.yandex.practicum.filmorate.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class OneOfValidator implements ConstraintValidator<OneOf, String> {
    private List<String> allowedValues;
    private boolean register;

    @Override
    public void initialize(OneOf constraintAnnotation) {
        this.allowedValues = Arrays.asList(constraintAnnotation.values());
        this.register = constraintAnnotation.isRegisterUp();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            System.out.println((allowedValues));
            return false;
            //null считаем не валидным
        }
        if (register) value = value.toUpperCase();
        for (String str : allowedValues) {
            if (str.equals(value)) return true;
        }
        return false;
    }
}