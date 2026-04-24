package edu.tcu.projectpulse.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String objectName, String fieldName, Object fieldValue) {
        super(objectName + " not found with " + fieldName + ": " + fieldValue);
    }
}
