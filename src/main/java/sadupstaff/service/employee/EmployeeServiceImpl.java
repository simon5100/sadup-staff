package sadupstaff.service.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sadupstaff.dto.request.create.CreateEmployeeRequest;
import sadupstaff.dto.request.update.UpdateEmployeeRequest;
import sadupstaff.dto.response.EmployeeResponse;
import sadupstaff.entity.management.Department;
import sadupstaff.mapper.employee.CreateEmployeeMapper;
import sadupstaff.mapper.employee.FindEmployeeMapper;
import sadupstaff.mapper.employee.UpdateEmployeeMapper;
import sadupstaff.repository.EmploeeyRepository;
import sadupstaff.entity.management.Employee;
import sadupstaff.service.department.DepartmentServiceImpl;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmploeeyRepository emploeeyRepository;
    private final DepartmentServiceImpl departmentService;
    private final UpdateEmployeeMapper updateEmployeeMapper;
    private final FindEmployeeMapper findEmployeeMapper;
    private final CreateEmployeeMapper createEmployeeMapper;

    @Override
    @Transactional
    public List<EmployeeResponse> getAllEmployees() {
        return emploeeyRepository.findAll().stream()
                .map(employee -> findEmployeeMapper.entityToResponse(employee))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmployeeResponse getEmployee(UUID id) {
        Optional<Employee> employeeOptional = emploeeyRepository.findById(id);
        if (employeeOptional.isPresent()) {
            return findEmployeeMapper.entityToResponse(employeeOptional.get());
        }
        return null;
    }

    @Override
    public Employee getEmployeeByIdForUpdate(UUID id) {
        Optional<Employee> employeeOptional = emploeeyRepository.findById(id);
        if (employeeOptional.isPresent()) {
            return employeeOptional.get();
        }
        return null;
    }

    @Override
    @Transactional
    public EmployeeResponse saveEmployee(CreateEmployeeRequest createEmployeeRequest) {
        Employee employee = createEmployeeMapper.toEntity(createEmployeeRequest);
        Department department = departmentService.getDepartmentByName(createEmployeeRequest.getDepartmentName());
        employee.setDepartment(department);
        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());
        employee = emploeeyRepository.save(employee);

        return getEmployee(employee.getId());
    }

    @Override
    @Transactional
    public EmployeeResponse updateEmployee(UUID id, UpdateEmployeeRequest updateData) {
        Employee employeeOld = getEmployeeByIdForUpdate(id);
        updateEmployeeMapper.updateEmployeeData(updateData, employeeOld);
        employeeOld.setUpdatedAt(LocalDateTime.now());
        emploeeyRepository.save(employeeOld);

        return findEmployeeMapper.entityToResponse(employeeOld);
    }

    @Override
    @Transactional
    public void deleteEmployee(UUID id){
        emploeeyRepository.deleteById(id);
    }
}