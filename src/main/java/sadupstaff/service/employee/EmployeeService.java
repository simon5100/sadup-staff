package sadupstaff.service.employee;

import sadupstaff.dto.management.employee.EmployeeDTO;
import sadupstaff.dto.management.employee.UpdateEmployeeDTO;
import sadupstaff.entity.management.Employee;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {

    public List<EmployeeDTO> getAllEmployees();

    public EmployeeDTO getEmployee(UUID id);

    public UUID saveEmployee(EmployeeDTO employeeDTO);

    public void updateEmployee(UUID id, UpdateEmployeeDTO updateEmployeeDTO);

    public Employee addEmployeeInDepartment(EmployeeDTO employeeDTO);

    public void deleteEmployee(UUID id);
}
