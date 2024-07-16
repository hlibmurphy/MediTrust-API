package com.github.meditrust.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentTimeValidator implements ConstraintValidator<ValidAppointmentTime, Object> {

    private String startTimeFieldName;
    private String dateFieldName;

    @Override
    public void initialize(ValidAppointmentTime constraintAnnotation) {
        this.startTimeFieldName = constraintAnnotation.startTime();
        this.dateFieldName = constraintAnnotation.date();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        try {
            Class<?> clazz = obj.getClass();
            Field dateField = clazz.getDeclaredField(dateFieldName);
            Field timeField = clazz.getDeclaredField(startTimeFieldName);

            dateField.setAccessible(true);
            timeField.setAccessible(true);

            LocalDate date = (LocalDate) dateField.get(obj);
            LocalTime time = (LocalTime) timeField.get(obj);

            if (date.isBefore(LocalDate.now())) {
                return false;
            }

            if (date.isEqual(LocalDate.now()) && time.isBefore(LocalTime.now())) {
                return false;
            }

            return true;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }
    }
}
