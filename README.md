# Employee Management System (EMS)

A modular, production-ready Java console application designed to manage employee records within an organisation. The project exemplifies clean architecture, Object-Oriented Programming (OOP) principles, Java 8 Stream API processing, deterministic collections, and structured custom exception handling.

---

## Project Structure

```
D:\FullStackAssignment\employee-management-system
├── src
│   └── com
│       └── company
│           └── ems
│               ├── model
│               │   └── Employee.java
│               ├── exception
│               │   ├── DuplicateEmployeeException.java
│               │   └── EmployeeNotFoundException.java
│               ├── service
│               │   ├── EmployeeService.java
│               │   └── EmployeeServiceImpl.java
│               └── Main.java
├── docs
│   └── output.txt
├── README.md
└── .gitignore
```

---

## Architectural Decisions & Design Principles

### 1. Domain Encapsulation
The `Employee` class encapsulates state using private fields (`id`, `name`, `department`, `salary`, `active`). Standard accessors and mutators govern access. Identity is established strictly via the employee `id` field within `equals()` and `hashCode()`, ensuring consistent behaviour when stored in hash-based data structures. The `toString()` method formats records into aligned columns suitable for terminal rendering.

### 2. Interface Abstraction (OOP)
Business logic is decoupled through the `EmployeeService` interface. Presentation components interact solely with the service abstraction rather than concrete storage mechanisms, adhering to the Dependency Inversion Principle (DIP).

### 3. Collections & Data Storage
`EmployeeServiceImpl` employs an in-memory `LinkedHashMap<Integer, Employee>`:
- **$O(1)$ Lookup Efficiency:** Lookups by employee ID execute in constant time.
- **Deterministic Ordering:** Maintains insertion order, ensuring consistent display output across menu executions.
- **Defensive Immutability:** `getAllEmployees()` returns an unmodifiable list via `Collections.unmodifiableList()`, preventing accidental external mutation of internal repository state.

### 4. Custom Exception Handling & Data Integrity
The application defines two checked exceptions to enforce strict business rules:
- **`DuplicateEmployeeException`:** Thrown when attempting to register an employee whose identifier already exists in the repository. The application validates ID uniqueness straightaway upon entry, preventing redundant data collection and protecting repository integrity.
- **`EmployeeNotFoundException`:** Thrown when an employee record cannot be resolved by ID during search or update operations.

The presentation layer catches these exceptions explicitly to present clear feedback to the user without crashing the application or returning unsafe `null` references.

### 5. Flexible Employee Updates
The `updateEmployee` service operation allows field-level modifications of existing employee records. Callers supply the target ID alongside values for Name, Department, Salary, or Active status, where non-null arguments selectively overwrite existing fields.

### 6. Stream API & Functional Filtering
Query methods leverage Java 8 Streams and method references:
- **Active High-Earners:** Filtered using chained predicates:
  ```java
  employeeRepository.values().stream()
      .filter(Employee::isActive)
      .filter(e -> e.getSalary() > minSalary)
      .collect(Collectors.toList());
  ```
- **Department Queries:** Case-insensitive comparisons (`equalsIgnoreCase`) after trimming whitespace, safeguarding queries against human input variance.

### 7. Terminal Input Hygiene
All console input reading uses `Scanner.nextLine()` parsed defensively via `Integer.parseInt()` and `Double.parseDouble()`. This avoids common scanner bugs associated with leftover newline characters in standard input buffers.

---

## Initial Seed Dataset

The application pre-populates four standard employee records upon startup:

| ID  | Name  | Department  | Salary (£)  | Status   |
|:---:|:------|:------------|:-----------:|:--------:|
| 101 | Alex  | Engineering | 90,000.00   | Active   |
| 102 | Sam   | Engineering | 125,000.00  | Active   |
| 103 | John  | Finance     | 140,000.00  | Inactive |
| 104 | Priya | Engineering | 150,000.00  | Active   |

---

## Build & Execution Instructions

### Prerequisites
- Java Development Kit (JDK 17 or higher).
- Terminal environment (WSL2, Linux, or Windows PowerShell/Command Prompt).

### Compilation
From the project root (`D:\FullStackAssignment\employee-management-system`):

```bash
# Compile all source files into the bin directory
javac -d bin src/com/company/ems/model/*.java \
             src/com/company/ems/exception/*.java \
             src/com/company/ems/service/*.java \
             src/com/company/ems/Main.java
```

On Windows Command Prompt:
```cmd
javac -d bin src\com\company\ems\model\*.java src\com\company\ems\exception\*.java src\com\company\ems\service\*.java src\com\company\ems\Main.java
```

### Execution

Run the interactive console application:
```bash
java -cp bin com.company.ems.Main
```

Menu options available:
```
1. View All Employees
2. Add New Employee
3. Search Employee by ID
4. Update Existing Employee
5. Filter Employees by Department
6. Filter Active Employees by Minimum Salary
7. Exit
```

---

## Sample Console Output

Below is an authentic terminal session illustrating duplicate ID prevention, record updates, and queries:

```
$ java -cp bin com.company.ems.Main
=================================================
     EMPLOYEE MANAGEMENT SYSTEM (EMS)
=================================================
--- Main Menu ---
1. View All Employees
2. Add New Employee
3. Search Employee by ID
4. Update Existing Employee
5. Filter Employees by Department
6. Filter Active Employees by Minimum Salary
7. Exit
Enter choice (1-7): 1

>>> All Registered Employees
--------------------------------------------------------------------
ID     | Name             | Department       | Salary      | Status
--------------------------------------------------------------------
101    | Alex             | Engineering      | £90000.00   | Active
102    | Sam              | Engineering      | £125000.00  | Active
103    | John             | Finance          | £140000.00  | Inactive
104    | Priya            | Engineering      | £150000.00  | Active
--------------------------------------------------------------------
Total records: 4

--- Main Menu ---
1. View All Employees
2. Add New Employee
3. Search Employee by ID
4. Update Existing Employee
5. Filter Employees by Department
6. Filter Active Employees by Minimum Salary
7. Exit
Enter choice (1-7): 2

>>> Add New Employee
Enter employee ID: 102
Error: Employee with ID 102 already exists.

--- Main Menu ---
1. View All Employees
2. Add New Employee
3. Search Employee by ID
4. Update Existing Employee
5. Filter Employees by Department
6. Filter Active Employees by Minimum Salary
7. Exit
Enter choice (1-7): 4

>>> Update Existing Employee
Enter employee ID: 102
Current details:
--------------------------------------------------------------------
ID     | Name             | Department       | Salary      | Status
--------------------------------------------------------------------
102    | Sam              | Engineering      | £125000.00  | Active
--------------------------------------------------------------------
Total records: 1
Select field to update:
1. Name
2. Department
3. Salary
4. Status (Active/Inactive)
Enter choice (1-4): 3
Enter new salary (£): 135000
Employee Updation Successful

--- Main Menu ---
1. View All Employees
2. Add New Employee
3. Search Employee by ID
4. Update Existing Employee
5. Filter Employees by Department
6. Filter Active Employees by Minimum Salary
7. Exit
Enter choice (1-7): 1

>>> All Registered Employees
--------------------------------------------------------------------
ID     | Name             | Department       | Salary      | Status
--------------------------------------------------------------------
101    | Alex             | Engineering      | £90000.00   | Active
102    | Sam              | Engineering      | £135000.00  | Active
103    | John             | Finance          | £140000.00  | Inactive
104    | Priya            | Engineering      | £150000.00  | Active
--------------------------------------------------------------------
Total records: 4

--- Main Menu ---
1. View All Employees
2. Add New Employee
3. Search Employee by ID
4. Update Existing Employee
5. Filter Employees by Department
6. Filter Active Employees by Minimum Salary
7. Exit
Enter choice (1-7): 6

>>> Filter Active Employees by Minimum Salary
Enter minimum salary (£): 100000
Active employees earning strictly above £100,000.00:
--------------------------------------------------------------------
ID     | Name             | Department       | Salary      | Status
--------------------------------------------------------------------
102    | Sam              | Engineering      | £135000.00  | Active
104    | Priya            | Engineering      | £150000.00  | Active
--------------------------------------------------------------------
Total records: 2

--- Main Menu ---
1. View All Employees
2. Add New Employee
3. Search Employee by ID
4. Update Existing Employee
5. Filter Employees by Department
6. Filter Active Employees by Minimum Salary
7. Exit
Enter choice (1-7): 3

>>> Search Employee by ID
Enter employee ID: 999
Error: Employee with ID 999 was not found.

--- Main Menu ---
1. View All Employees
2. Add New Employee
3. Search Employee by ID
4. Update Existing Employee
5. Filter Employees by Department
6. Filter Active Employees by Minimum Salary
7. Exit
Enter choice (1-7): 7

Exiting application. Thank you for using EMS.
```

Full logs are available in [docs/output.txt](file:///mnt/d/FullStackAssignment/employee-management-system/docs/output.txt).
