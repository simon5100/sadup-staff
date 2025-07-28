package sadupstaff.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sadupstaff.dto.management.employee.EmployeeDTO;
import sadupstaff.dto.management.employee.UpdateEmployeeDTO;
import sadupstaff.mapper.management.employee.MapperCreateEmployee;
import sadupstaff.mapper.management.employee.MapperEmployee;
import sadupstaff.mapper.management.employee.MapperFindEmployee;
import sadupstaff.mapper.management.employee.MapperUpdateEmployee;
import sadupstaff.model.employee.CreateRequestEmployee;
import sadupstaff.model.employee.ResponseEmployee;
import sadupstaff.model.employee.UpdateRequestEmployee;
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
    private final MapperFindEmployee findIdEmployee;
    private final MapperUpdateEmployee updateEmployee;
    private final MapperUpdateEmployee mapperUpdateEmployee;
    private final MapperEmployee mapperEmployee;

    @GetMapping("/v1/employees")
    public List<ResponseEmployee> showAllEmployees() {
        return employeeService.getAllEmployees().stream()
                .map(employeeDTO -> findIdEmployee.employeeDTOToEmployeeResponse(employeeDTO))
                .collect(Collectors.toList());
    }

    @GetMapping("/v1/employees/{id}")
    public ResponseEmployee getEmployee(@PathVariable UUID id) {
        EmployeeDTO employeeDTO = employeeService.getEmployee(id);
        return findIdEmployee.employeeDTOToEmployeeResponse(employeeDTO);
    }

    @PostMapping("/v1/employees")
    public ResponseEmployee addEmployee(@RequestBody CreateRequestEmployee createRequestEmployee) {
        EmployeeDTO employeeDTO = createEmployee.toDto(createRequestEmployee);
        return getEmployee(employeeService.saveEmployee(employeeDTO));
    }

    @PutMapping("/v1/employees/{id}")
    public ResponseEmployee updateEmployee(@PathVariable UUID id, @RequestBody UpdateRequestEmployee updateRequestEmployee) {
        UpdateEmployeeDTO updateEmployeeDTO = mapperUpdateEmployee
                .updateRequestEmployeetoUpdateEmployeeDTO(updateRequestEmployee);
        employeeService.updateEmployee(id, updateEmployeeDTO);
        return getEmployee(id);
    }

    @DeleteMapping("/v1/employees/{id}")
    public void deleteEmployee(@PathVariable UUID id) {
        employeeService.deleteEmployee(id);
    }
}