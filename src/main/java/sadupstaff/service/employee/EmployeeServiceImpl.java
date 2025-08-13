package sadupstaff.service.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sadupstaff.dto.employee.UpdateEmployeeDTO;
import sadupstaff.dto.request.employee.CreateEmployeeRequest;
import sadupstaff.dto.request.employee.UpdateEmployeeRequest;
import sadupstaff.dto.response.EmployeeResponse;
import sadupstaff.entity.management.Department;
import sadupstaff.mapper.employee.MapperCreateEmployee;
import sadupstaff.mapper.employee.MapperFindEmployee;
import sadupstaff.mapper.employee.MapperUpdateEmployee;
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
    private final MapperUpdateEmployee mapperUpdateEmployee;
    private final MapperFindEmployee mapperFindEmployee;
    private final MapperCreateEmployee mapperCreateEmployee;

    @Override
    @Transactional
    public List<EmployeeResponse> getAllEmployees() {
        return emploeeyRepository.findAll().stream()
                .map(employee -> mapperFindEmployee.entityToResponse(employee))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmployeeResponse getEmployee(UUID id) {
        Optional<Employee> employeeOptional = emploeeyRepository.findById(id);
        if (employeeOptional.isPresent()) {
            return mapperFindEmployee.entityToResponse(employeeOptional.get());
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
        Employee employee = mapperCreateEmployee.toEntity(createEmployeeRequest);
        Department department = departmentService.getDepartmentByName(createEmployeeRequest.getDepartmentName());
        employee.setDepartment(department);
        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());
        employee = emploeeyRepository.save(employee);

        return getEmployee(employee.getId());
    }

    @Override
    @Transactional
    public EmployeeResponse updateEmployee(UUID id, UpdateEmployeeRequest updateRequest) {
        UpdateEmployeeDTO updateData = mapperUpdateEmployee
                .updateRequestEmployeeToUpdateEmployeeDTO(updateRequest);
        UpdateEmployeeDTO updateEmployeeOld = mapperUpdateEmployee
                .entityToUpdateEmployeeDTO(getEmployeeByIdForUpdate(id));
        mapperUpdateEmployee.updateEmployeeData(updateData, updateEmployeeOld);
        Employee employee = mapperUpdateEmployee.updateEmployeeDTOToEntity(updateEmployeeOld);
        employee.setUpdatedAt(LocalDateTime.now());
        emploeeyRepository.save(employee);

        return getEmployee(id);
    }

    @Override
    @Transactional
    public void deleteEmployee(UUID id){
        emploeeyRepository.deleteById(id);
    }
}