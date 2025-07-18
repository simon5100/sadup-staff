package sadupstaff.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sadupstaff.entity.management.Employee;
import sadupstaff.service.employee.EmployeeService;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmployeeRESTController {

    private final EmployeeService employeeService;

    @GetMapping("/v1/employees")
    public List<Employee> showAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/v1/employees/{id}")
    public Employee getEmployee(@PathVariable UUID id) {
        Employee employee = employeeService.getEmployee(id);
        return employee;
    }

    @PostMapping("/v1/employees")
    public Employee addEmployee(@RequestBody Employee employee) {
        employeeService.saveEmployee(employee);
        return employee;
    }

    @PutMapping("/v1/employees/{id}")
    public Employee updateEmployee(@PathVariable UUID id, @RequestBody Employee employee) {
        employeeService.updateEmployee(id, employee);
        return employeeService.getEmployee(id);
    }

    @DeleteMapping("/v1/employees/{id}")
    public void deleteEmployee(@PathVariable UUID id) {
        employeeService.deleteEmployee(id);
    }
}