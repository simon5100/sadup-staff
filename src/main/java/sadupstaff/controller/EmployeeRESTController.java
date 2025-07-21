package sadupstaff.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sadupstaff.entity.management.Employee;
import sadupstaff.mapper.management.employee.MapperCreateEmployee;
import sadupstaff.mapper.management.employee.MapperFindAllEmployee;
import sadupstaff.mapper.management.employee.MapperFindIdEmployee;
import sadupstaff.mapper.management.employee.MapperUpdateEmployee;
import sadupstaff.model.employee.ResponseEmployee;
import sadupstaff.service.employee.EmployeeService;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmployeeRESTController {

    private final EmployeeService employeeService;
    private final MapperCreateEmployee createEmployee;
    private final MapperFindIdEmployee findIdEmployee;
    private final MapperUpdateEmployee updateEmployee;
    private final MapperFindAllEmployee findAllEmployee;


    @GetMapping("/v1/employees")
    public List<ResponseEmployee> showAllEmployees() {
        return employeeService.getAllEmployees().stream()
                .map(employeeDTO -> findIdEmployee.employeeDTOToEmployeeResponse(employeeDTO))
                .collect(Collectors.toList());
    }

//    @GetMapping("/v1/employees/{id}")
//    public ResponseEmployee getEmployee(@PathVariable UUID id) {
//        Employee employee = employeeService.getEmployee(id);
//        return employee;
//    }

//    @PostMapping("/v2/employees")
//    public ResponseEmployee addEmployee(@RequestBody CreateRequestEmployee createRequestEmployee) {
//        EmployeeDTO employeeDTO = createEmployee.toDto(createRequestEmployee);
//        Employee employee = createEmployee.toEntity(employeeDTO);
//        employeeService.saveEmployee(employee);
//
//
//        return new ResponseEmployee();
//    }

//

    @DeleteMapping("/v1/employees/{id}")
    public void deleteEmployee(@PathVariable UUID id) {
        employeeService.deleteEmployee(id);
    }
}