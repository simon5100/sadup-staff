package sadupstaff.service.employee;

import sadupstaff.dto.management.EmployeeDTO;
import sadupstaff.entity.management.Employee;
import java.util.List;
import java.util.UUID;

public interface EmployeeService {

    public List<EmployeeDTO> getAllEmployees();

    public EmployeeDTO getEmployee(UUID id);

    public void saveEmployee(EmployeeDTO employeeDTO);

    public void updateEmployee(UUID id, EmployeeDTO employeeDTO);

    public void deleteEmployee(UUID id);
}
