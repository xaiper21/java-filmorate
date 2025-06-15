package ru.yandex.practicum.filmorate.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateNoEarlierThanValidator implements ConstraintValidator<DateNoEarlierThan, LocalDate> {
    private LocalDate noEarlierDate;
    private DateTimeFormatter formatter;

    @Override
    public void initialize(DateNoEarlierThan constraintAnnotation) {
        this.formatter = DateTimeFormatter.ofPattern(constraintAnnotation.format());
        this.noEarlierDate = LocalDate.parse(constraintAnnotation.date(), formatter);
    }

    @Override
    public boolean isValid(LocalDate s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) return false; //при null считаем не валидным
        return s.isAfter(noEarlierDate);
    }
}
