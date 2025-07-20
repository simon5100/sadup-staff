package sadupstaff.controller.restv2;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sadupstaff.dto.management.EmployeeDTO;
import sadupstaff.entity.management.Employee;
import sadupstaff.mapper.management.employee.MapperCreateEmployee;
import sadupstaff.model.employee.CreateRequestEmployee;
import sadupstaff.model.employee.ResponseEmployee;
import sadupstaff.service.employee.EmployeeService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmployeeRESTController {

    private final EmployeeService employeeService;

    private final MapperCreateEmployee createEmployee;

    @GetMapping("/v2/employees")
    public List<Employee> showAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/v2/employees/{id}")
    public Employee getEmployee(@PathVariable UUID id) {
        Employee employee = employeeService.getEmployee(id);
        return employee;
    }

    @PostMapping("/v2/employees")
    public ResponseEmployee addEmployee(@RequestBody CreateRequestEmployee createRequestEmployee) {
        EmployeeDTO employeeDTO = createEmployee.toDto(createRequestEmployee);

        employeeService.saveEmployee(employee);
        return employee;
    }

    @PutMapping("/v2/employees/{id}")
    public Employee updateEmployee(@PathVariable UUID id, @RequestBody Employee employee) {
        employeeService.updateEmployee(id, employee);
        return employeeService.getEmployee(id);
    }

    @DeleteMapping("/v2/employees/{id}")
    public void deleteEmployee(@PathVariable UUID id) {
        employeeService.deleteEmployee(id);
    }
}