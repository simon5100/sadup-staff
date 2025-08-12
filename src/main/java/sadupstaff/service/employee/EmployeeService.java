package sadupstaff.service.employee;

import sadupstaff.dto.employee.EmployeeDTO;
import sadupstaff.dto.request.employee.CreateRequestEmployee;
import sadupstaff.dto.request.employee.UpdateRequestEmployee;
import sadupstaff.dto.response.ResponseEmployee;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {

    List<ResponseEmployee> getAllEmployees();

    ResponseEmployee getEmployee(UUID id);

    EmployeeDTO getEmployeeByIdForUpdate(UUID id);

    ResponseEmployee saveNewEmployee(CreateRequestEmployee createRequestEmployee);

    ResponseEmployee updateEmployee(UUID id, UpdateRequestEmployee updateRequestEmployee);

    void deleteEmployee(UUID id);
}
