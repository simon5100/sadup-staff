package sadupstaff.controller.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sadupstaff.dto.request.employee.CreateRequestEmployee;
import sadupstaff.dto.response.ResponseEmployee;
import sadupstaff.dto.request.employee.UpdateRequestEmployee;
import sadupstaff.service.employee.EmployeeService;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class EmployeeRESTControllerImpl implements EmployeeRESTController {

    private final EmployeeService employeeService;

    public List<ResponseEmployee> showAllEmployees() {
        return employeeService.getAllEmployees();
    }

    public ResponseEmployee getEmployee(@PathVariable UUID id) {
        return employeeService.getEmployee(id);
    }

    public ResponseEmployee addEmployee(@RequestBody CreateRequestEmployee createRequestEmployee) {
        return employeeService.saveNewEmployee(createRequestEmployee);
    }

    public ResponseEmployee updateEmployee(@PathVariable UUID id, @RequestBody UpdateRequestEmployee updateRequestEmployee) {
        return employeeService.updateEmployee(id, updateRequestEmployee);
    }

    public void deleteEmployee(@PathVariable UUID id) {
        employeeService.deleteEmployee(id);
    }
}