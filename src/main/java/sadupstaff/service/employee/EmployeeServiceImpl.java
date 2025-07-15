package sadupstaff.service.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sadupstaff.repository.EmploeeyRepository;
import sadupstaff.entity.management.Employee;
import sadupstaff.service.department_servic.DepartmentServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmploeeyRepository emploeeyRepository;

    private final DepartmentServiceImpl departmentService;

    @Override
    @Transactional
    public List<Employee> getAllEmployees() {
        return emploeeyRepository.findAll();
    }

    @Override
    @Transactional
    public Employee getEmployee(UUID id) {
        Employee employee = null;
        Optional<Employee> employeeOptional = emploeeyRepository.findById(id);
        if (employeeOptional.isPresent()) {
            employee = employeeOptional.get();
        }

        return employee;
    }

    @Override
    @Transactional
    public void saveEmployee(Employee employee) {
        emploeeyRepository.save(employee);
    }

    @Override
    @Transactional
    public void deleteEmployee(UUID id){
        emploeeyRepository.deleteById(id);

    }
}
