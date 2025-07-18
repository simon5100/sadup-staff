package sadupstaff.service.employee;

import sadupstaff.entity.management.Employee;
import java.util.List;
import java.util.UUID;

public interface EmployeeService {

    public List<Employee> getAllEmployees();

    public Employee getEmployee(UUID id);

    public void saveEmployee(Employee employee);

    public void updateEmployee(UUID id, Employee employeeNew);

    public void deleteEmployee(UUID id);
}
