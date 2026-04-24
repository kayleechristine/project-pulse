package edu.tcu.projectpulse.shared.validation;

import edu.tcu.projectpulse.exception.ValidationException;

public final class ValidationUtils {

    private ValidationUtils() {}

    public static void assertNotEmpty(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new ValidationException(fieldName + " must not be empty");
        }
    }

    public static void assertNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new ValidationException(fieldName + " must not be null");
        }
    }

    public static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new ValidationException(message);
        }
    }

    public static void assertMaxLength(String value, String fieldName, int max) {
        if (value != null && value.length() > max) {
            throw new ValidationException(fieldName + " must not exceed " + max + " characters");
        }
    }
}
