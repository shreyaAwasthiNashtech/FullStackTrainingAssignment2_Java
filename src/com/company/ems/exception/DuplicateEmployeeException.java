package com.company.ems.exception;

/**
 * Checked exception thrown when attempting to add an employee whose
 * identifier already exists within the repository.
 */
public class DuplicateEmployeeException extends Exception {

    public DuplicateEmployeeException(String message) {
        super(message);
    }

    public DuplicateEmployeeException(String message, Throwable cause) {
        super(message, cause);
    }
}
