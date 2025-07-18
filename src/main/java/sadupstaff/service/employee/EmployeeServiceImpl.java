package sadupstaff.service.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sadupstaff.repository.EmploeeyRepository;
import sadupstaff.entity.management.Employee;
import sadupstaff.service.UpdatingData;
import sadupstaff.service.department.DepartmentServiceImpl;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmploeeyRepository emploeeyRepository;

    private final DepartmentServiceImpl departmentService;

    private final UpdatingData updatingData;

    @Override
    @Transactional
    public List<Employee> getAllEmployees() {
        return emploeeyRepository.findAll();
    }

    @Override
    @Transactional
    public Employee getEmployee(UUID id) {
        Optional<Employee> employeeOptional = emploeeyRepository.findById(id);
        if (employeeOptional.isPresent()) {
            return employeeOptional.get();
        }
        return null;
    }

    @Override
    @Transactional
    public void saveEmployee(Employee employee) {
        emploeeyRepository.save(employee);
    }

    @Override
    @Transactional
    public void updateEmployee(UUID id, Employee employeeUpdate) {
        Employee employee = getEmployee(id);
        emploeeyRepository.save(updatingData.updatingData(employee, employeeUpdate, Employee.class));
    }

    @Override
    @Transactional
    public void deleteEmployee(UUID id){
        emploeeyRepository.deleteById(id);
    }
}