package com.company.ems.service;

import com.company.ems.exception.DuplicateEmployeeException;
import com.company.ems.exception.EmployeeNotFoundException;
import com.company.ems.model.Employee;

import java.util.List;

/**
 * Service contract defining core employee management operations.
 */
public interface EmployeeService {

    /**
     * Adds an employee to the repository.
     *
     * @param employee the employee record to store
     * @throws DuplicateEmployeeException if an employee with the same ID already exists
     */
    void addEmployee(Employee employee) throws DuplicateEmployeeException;

    /**
     * Checks whether an employee exists with the supplied numerical identifier.
     *
     * @param id the employee identifier
     * @return true if an employee exists with this ID, false otherwise
     */
    boolean existsById(int id);

    /**
     * Updates an existing employee's details. Only non-null arguments are applied.
     *
     * @param id         the unique identifier of the employee to update
     * @param name       the new name, or null to leave unchanged
     * @param department the new department, or null to leave unchanged
     * @param salary     the new salary, or null to leave unchanged
     * @param active     the new active status, or null to leave unchanged
     * @throws EmployeeNotFoundException if no employee exists with the supplied ID
     */
    void updateEmployee(int id, String name, String department, Double salary, Boolean active)
            throws EmployeeNotFoundException;

    /**
     * Retrieves all employees currently managed by the service.
     *
     * @return a list containing all employee records
     */
    List<Employee> getAllEmployees();

    /**
     * Finds an employee by their unique numerical identifier.
     *
     * @param id the employee identifier
     * @return the matching employee entity
     * @throws EmployeeNotFoundException if no employee exists with the supplied ID
     */
    Employee findById(int id) throws EmployeeNotFoundException;

    /**
     * Finds all employees assigned to a specific department.
     *
     * @param department the department name to filter by
     * @return a list of matching employees
     */
    List<Employee> findByDepartment(String department);

    /**
     * Retrieves all active employees earning strictly above a specified salary threshold.
     *
     * @param minSalary the minimum salary threshold
     * @return a list of active employees meeting the salary criteria
     */
    List<Employee> findActiveWithSalaryAbove(double minSalary);
}
