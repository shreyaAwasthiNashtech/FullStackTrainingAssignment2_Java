package com.company.ems;

import com.company.ems.exception.DuplicateEmployeeException;
import com.company.ems.exception.EmployeeNotFoundException;
import com.company.ems.model.Employee;
import com.company.ems.service.EmployeeService;
import com.company.ems.service.EmployeeServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Entry point for the Employee Management System console application.
 * Provides an interactive console menu interface for managing employee records.
 */
public class Main {

    private static final String TABLE_DIVIDER = "--------------------------------------------------------------------";

    public static void main(String[] args) {
        EmployeeService employeeService = new EmployeeServiceImpl();
        runInteractiveMenu(employeeService);
    }

    /**
     * Executes the interactive console menu loop.
     *
     * @param service the employee service instance
     */
    private static void runInteractiveMenu(EmployeeService service) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=================================================");
        System.out.println("     EMPLOYEE MANAGEMENT SYSTEM (EMS)");
        System.out.println("=================================================");

        while (running) {
            printMenu();
            System.out.print("Enter choice (1-7): ");

            if (!scanner.hasNextLine()) {
                break;
            }

            String choice = scanner.nextLine().trim();
            System.out.println();

            switch (choice) {
                case "1":
                    handleViewAll(service);
                    break;
                case "2":
                    handleAddNewEmployee(service, scanner);
                    break;
                case "3":
                    handleSearchById(service, scanner);
                    break;
                case "4":
                    handleUpdateEmployee(service, scanner);
                    break;
                case "5":
                    handleFilterByDepartment(service, scanner);
                    break;
                case "6":
                    handleFilterActiveBySalary(service, scanner);
                    break;
                case "7":
                    System.out.println("Exiting application. Thank you for using EMS.");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid selection. Please enter a number between 1 and 7.");
            }
            System.out.println();
        }
    }

    private static void printMenu() {
        System.out.println("--- Main Menu ---");
        System.out.println("1. View All Employees");
        System.out.println("2. Add New Employee");
        System.out.println("3. Search Employee by ID");
        System.out.println("4. Update Existing Employee");
        System.out.println("5. Filter Employees by Department");
        System.out.println("6. Filter Active Employees by Minimum Salary");
        System.out.println("7. Exit");
    }

    private static void handleViewAll(EmployeeService service) {
        System.out.println(">>> All Registered Employees");
        List<Employee> employees = service.getAllEmployees();
        printEmployeeTable(employees);
    }

    private static void handleAddNewEmployee(EmployeeService service, Scanner scanner) {
        System.out.println(">>> Add New Employee");

        int id = readInt(scanner, "Enter employee ID: ");
        if (id < 0) return;

        try {
            if (service.existsById(id)) {
                throw new DuplicateEmployeeException("Employee with ID " + id + " already exists.");
            }
        } catch (DuplicateEmployeeException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        String name = readNonEmptyString(scanner, "Enter employee name: ");
        if (name.isEmpty()) return;

        String department = readNonEmptyString(scanner, "Enter department: ");
        if (department.isEmpty()) return;

        double salary = readDouble(scanner, "Enter salary (£): ");
        if (salary < 0) return;

        boolean active = readBoolean(scanner, "Is active (true/false or y/n): ");

        Employee employee = new Employee(id, name, department, salary, active);
        try {
            service.addEmployee(employee);
            System.out.println("Employee '" + name + "' (ID: " + id + ") added successfully.");
        } catch (DuplicateEmployeeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleSearchById(EmployeeService service, Scanner scanner) {
        System.out.println(">>> Search Employee by ID");
        int id = readInt(scanner, "Enter employee ID: ");
        if (id < 0) return;

        try {
            Employee employee = service.findById(id);
            System.out.println("Employee found:");
            printEmployeeTable(Collections.singletonList(employee));
        } catch (EmployeeNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleUpdateEmployee(EmployeeService service, Scanner scanner) {
        System.out.println(">>> Update Existing Employee");
        int id = readInt(scanner, "Enter employee ID: ");
        if (id < 0) return;

        try {
            Employee employee = service.findById(id);
            System.out.println("Current details:");
            printEmployeeTable(Collections.singletonList(employee));

            System.out.println("Select field to update:");
            System.out.println("1. Name");
            System.out.println("2. Department");
            System.out.println("3. Salary");
            System.out.println("4. Status (Active/Inactive)");
            System.out.print("Enter choice (1-4): ");

            if (!scanner.hasNextLine()) {
                return;
            }
            String fieldChoice = scanner.nextLine().trim();

            switch (fieldChoice) {
                case "1":
                    String newName = readNonEmptyString(scanner, "Enter new name: ");
                    if (newName.isEmpty()) return;
                    service.updateEmployee(id, newName, null, null, null);
                    break;
                case "2":
                    String newDepartment = readNonEmptyString(scanner, "Enter new department: ");
                    if (newDepartment.isEmpty()) return;
                    service.updateEmployee(id, null, newDepartment, null, null);
                    break;
                case "3":
                    double newSalary = readDouble(scanner, "Enter new salary (£): ");
                    if (newSalary < 0) return;
                    service.updateEmployee(id, null, null, newSalary, null);
                    break;
                case "4":
                    boolean newActive = readBoolean(scanner, "Is active (true/false or y/n): ");
                    service.updateEmployee(id, null, null, null, newActive);
                    break;
                default:
                    System.out.println("Invalid selection. Please choose an option between 1 and 4.");
                    return;
            }

            System.out.println("Employee Updation Successful");
        } catch (EmployeeNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleFilterByDepartment(EmployeeService service, Scanner scanner) {
        System.out.println(">>> Filter Employees by Department");
        String department = readNonEmptyString(scanner, "Enter department: ");
        if (department.isEmpty()) return;

        List<Employee> matching = service.findByDepartment(department);
        System.out.println("Employees in department '" + department + "':");
        printEmployeeTable(matching);
    }

    private static void handleFilterActiveBySalary(EmployeeService service, Scanner scanner) {
        System.out.println(">>> Filter Active Employees by Minimum Salary");
        double minSalary = readDouble(scanner, "Enter minimum salary (£): ");
        if (minSalary < 0) return;

        List<Employee> matching = service.findActiveWithSalaryAbove(minSalary);
        System.out.printf("Active employees earning strictly above £%,.2f:%n", minSalary);
        printEmployeeTable(matching);
    }

    /**
     * Formats and prints a tabular display of employees to standard output.
     *
     * @param employees the list of employees to display
     */
    public static void printEmployeeTable(List<Employee> employees) {
        if (employees == null || employees.isEmpty()) {
            System.out.println("No matching employee records found.");
            return;
        }
        System.out.println(TABLE_DIVIDER);
        System.out.printf("%-6s | %-16s | %-16s | %-11s | %s%n",
                "ID", "Name", "Department", "Salary", "Status");
        System.out.println(TABLE_DIVIDER);
        for (Employee emp : employees) {
            System.out.println(emp);
        }
        System.out.println(TABLE_DIVIDER);
        System.out.println("Total records: " + employees.size());
    }

    private static int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                return -1;
            }
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    private static double readDouble(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                return -1.0;
            }
            String input = scanner.nextLine().trim();
            try {
                double value = Double.parseDouble(input);
                if (value < 0) {
                    System.out.println("Salary cannot be negative. Please try again.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid decimal number.");
            }
        }
    }

    private static boolean readBoolean(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                return false;
            }
            String input = scanner.nextLine().trim().toLowerCase();
            if ("true".equals(input) || "yes".equals(input) || "y".equals(input)) {
                return true;
            }
            if ("false".equals(input) || "no".equals(input) || "n".equals(input)) {
                return false;
            }
            System.out.println("Invalid input. Please enter 'true'/'false' or 'y'/'n'.");
        }
    }

    private static String readNonEmptyString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                return "";
            }
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be blank. Please try again.");
        }
    }
}
