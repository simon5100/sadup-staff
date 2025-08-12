package sadupstaff.service.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sadupstaff.dto.employee.EmployeeDTO;
import sadupstaff.dto.employee.UpdateEmployeeDTO;
import sadupstaff.dto.request.employee.CreateRequestEmployee;
import sadupstaff.dto.request.employee.UpdateRequestEmployee;
import sadupstaff.dto.response.ResponseEmployee;
import sadupstaff.entity.management.Department;
import sadupstaff.mapper.employee.MapperCreateEmployee;
import sadupstaff.mapper.employee.MapperEmployee;
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
    private final MapperEmployee mapperEmployee;
    private final MapperFindEmployee mapperFindEmployee;
    private final MapperCreateEmployee mapperCreateEmployee;

    @Override
    @Transactional
    public List<ResponseEmployee> getAllEmployees() {
        return emploeeyRepository.findAll().stream()
                .map(employee -> mapperFindEmployee.employeeToEmployeeResponse(employee))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ResponseEmployee getEmployee(UUID id) {
        Optional<Employee> employeeOptional = emploeeyRepository.findById(id);
        if (employeeOptional.isPresent()) {
            return mapperFindEmployee.employeeToEmployeeResponse(employeeOptional.get());
        }
        return null;
    }

    @Override
    public EmployeeDTO getEmployeeByIdForUpdate(UUID id) {
        Optional<Employee> employeeOptional = emploeeyRepository.findById(id);
        if (employeeOptional.isPresent()) {
            return mapperEmployee.toDTO(employeeOptional.get());
        }
        return null;
    }

    @Override
    @Transactional
    public ResponseEmployee saveNewEmployee(CreateRequestEmployee createRequestEmployee) {
        Employee employee = mapperCreateEmployee.toEntity(createRequestEmployee);
        Department department = departmentService.getDepartmentByName(createRequestEmployee.getDepartmentName());
        employee.setDepartment(department);
        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());
        employee = emploeeyRepository.save(employee);

        return getEmployee(employee.getId());
    }

    @Override
    @Transactional
    public ResponseEmployee updateEmployee(UUID id, UpdateRequestEmployee updateRequestEmployee) {
        UpdateEmployeeDTO updateData = mapperUpdateEmployee
                .updateRequestEmployeetoUpdateEmployeeDTO(updateRequestEmployee);
        EmployeeDTO employeeDTO = getEmployeeByIdForUpdate(id);
        UpdateEmployeeDTO updateEmployeeDTO = mapperUpdateEmployee
                .EmployeeDTOToUpdateEmployeeDTO(employeeDTO);
        mapperUpdateEmployee.updateEmployeeDTO(updateData, updateEmployeeDTO);
        employeeDTO = mapperUpdateEmployee.updateEmployeeDTOToEmployeeDTO(updateEmployeeDTO);
        Employee employee = mapperEmployee.toEmployee(employeeDTO);
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