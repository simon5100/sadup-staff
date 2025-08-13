package sadupstaff.controller.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sadupstaff.dto.request.employee.CreateEmployeeRequest;
import sadupstaff.dto.request.employee.UpdateEmployeeRequest;
import sadupstaff.dto.response.EmployeeResponse;
import sadupstaff.service.employee.EmployeeService;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class EmployeeRESTControllerImpl implements EmployeeRESTController {

    private final EmployeeService employeeService;

    public List<EmployeeResponse> showAllEmployees() {
        return employeeService.getAllEmployees();
    }

    public EmployeeResponse getEmployee(@PathVariable UUID id) {
        return employeeService.getEmployee(id);
    }

    public EmployeeResponse addEmployee(@RequestBody CreateEmployeeRequest createEmployeeRequest) {
        return employeeService.saveEmployee(createEmployeeRequest);
    }

    public EmployeeResponse updateEmployee(@PathVariable UUID id, @RequestBody UpdateEmployeeRequest updateEmployeeRequest) {
        return employeeService.updateEmployee(id, updateEmployeeRequest);
    }

    public void deleteEmployee(@PathVariable UUID id) {
        employeeService.deleteEmployee(id);
    }
}