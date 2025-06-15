package ru.yandex.practicum.filmorate.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {DateNoEarlierThanValidator.class})
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DateNoEarlierThan {
    String message() default "Дата релиза — не раньше {date}";

    String date();

    String format();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
