package com.company.ems.exception;

/**
 * Checked exception thrown when an employee cannot be found by their identifier.
 */
public class EmployeeNotFoundException extends Exception {

    public EmployeeNotFoundException(String message) {
        super(message);
    }
}
