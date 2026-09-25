package com.company.ems.service;

import com.company.ems.exception.DuplicateEmployeeException;
import com.company.ems.exception.EmployeeNotFoundException;
import com.company.ems.model.Employee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * In-memory implementation of the {@link EmployeeService} backed by a {@link LinkedHashMap}
 * to guarantee deterministic insertion order and efficient lookups.
 */
public class EmployeeServiceImpl implements EmployeeService {

    private final Map<Integer, Employee> employeeRepository = new LinkedHashMap<>();

    /**
     * Initialises the service and seeds the repository with standard sample records.
     */
    public EmployeeServiceImpl() {
        seedInitialData();
    }

    /**
     * Pre-populates the repository with the specified default dataset.
     */
    private void seedInitialData() {
        try {
            addEmployee(new Employee(101, "Alex", "Engineering", 90000.0, true));
            addEmployee(new Employee(102, "Sam", "Engineering", 125000.0, true));
            addEmployee(new Employee(103, "John", "Finance", 140000.0, false));
            addEmployee(new Employee(104, "Priya", "Engineering", 150000.0, true));
        } catch (DuplicateEmployeeException e) {
            // Seed records are guaranteed to be unique
        }
    }

    @Override
    public void addEmployee(Employee employee) throws DuplicateEmployeeException {
        if (employee == null) {
            return;
        }
        if (employeeRepository.containsKey(employee.getId())) {
            throw new DuplicateEmployeeException("Employee with ID " + employee.getId() + " already exists.");
        }
        employeeRepository.put(employee.getId(), employee);
    }

    @Override
    public boolean existsById(int id) {
        return employeeRepository.containsKey(id);
    }

    @Override
    public void updateEmployee(int id, String name, String department, Double salary, Boolean active)
            throws EmployeeNotFoundException {
        Employee employee = findById(id);

        if (name != null && !name.trim().isEmpty()) {
            employee.setName(name.trim());
        }
        if (department != null && !department.trim().isEmpty()) {
            employee.setDepartment(department.trim());
        }
        if (salary != null && salary >= 0) {
            employee.setSalary(salary);
        }
        if (active != null) {
            employee.setActive(active);
        }

        employeeRepository.put(id, employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return Collections.unmodifiableList(new ArrayList<>(employeeRepository.values()));
    }

    @Override
    public Employee findById(int id) throws EmployeeNotFoundException {
        return Optional.ofNullable(employeeRepository.get(id))
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with ID " + id + " was not found."));
    }

    @Override
    public List<Employee> findByDepartment(String department) {
        if (department == null) {
            return Collections.emptyList();
        }
        return employeeRepository.values().stream()
                .filter(e -> e.getDepartment() != null && e.getDepartment().equalsIgnoreCase(department.trim()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Employee> findActiveWithSalaryAbove(double minSalary) {
        return employeeRepository.values().stream()
                .filter(Employee::isActive)
                .filter(e -> e.getSalary() > minSalary)
                .collect(Collectors.toList());
    }
}
