package sadupstaff.service.employee;

import sadupstaff.dto.request.create.CreateEmployeeRequest;
import sadupstaff.dto.request.update.UpdateEmployeeRequest;
import sadupstaff.dto.response.EmployeeResponse;
import sadupstaff.entity.management.Employee;
import java.util.List;
import java.util.UUID;

public interface EmployeeService {

    List<EmployeeResponse> getAllEmployees();

    EmployeeResponse getEmployee(UUID id);

    Employee getEmployeeByIdForUpdate(UUID id);

    EmployeeResponse saveEmployee(CreateEmployeeRequest createEmployeeRequest);

    EmployeeResponse updateEmployee(UUID id, UpdateEmployeeRequest updateEmployeeRequest);

    void deleteEmployee(UUID id);
}
