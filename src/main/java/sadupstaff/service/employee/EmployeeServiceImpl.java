package sadupstaff.service.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sadupstaff.dto.management.department.DepartmentDTO;
import sadupstaff.dto.management.employee.EmployeeDTO;
import sadupstaff.dto.management.employee.UpdateEmployeeDTO;
import sadupstaff.mapper.management.employee.MapperCreateEmployee;
import sadupstaff.mapper.management.employee.MapperFindIdEmployee;
import sadupstaff.mapper.management.employee.MapperUpdateEmployee;
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
    private final MapperFindIdEmployee mapperFindIdEmployee;
    private final MapperCreateEmployee mapperCreateEmployee;

    @Override
    @Transactional
    public List<EmployeeDTO> getAllEmployees() {

        return emploeeyRepository.findAll().stream()
                .map(employee -> mapperFindIdEmployee.employeeToEmployeeDTO(employee))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmployeeDTO getEmployee(UUID id) {
        Optional<Employee> employeeOptional = emploeeyRepository.findById(id);
        if (employeeOptional.isPresent()) {
            return mapperFindIdEmployee.employeeToEmployeeDTO(employeeOptional.get());
        }
        return null;
    }

    @Override
    @Transactional
    public UUID saveEmployee(EmployeeDTO employeeDTO) {
        Employee employee = mapperCreateEmployee.toEntity(employeeDTO);
        if (employee.getCreatedAt() == null) {
            employee.setCreatedAt(LocalDateTime.now());
        }
        employee.setUpdatedAt(LocalDateTime.now());
        return emploeeyRepository.save(employee).getId();
    }

    @Override
    @Transactional
    public void updateEmployee(UUID id, UpdateEmployeeDTO updateData) {
        EmployeeDTO employeeDTO = getEmployee(id);
        UpdateEmployeeDTO updateEmployeeDTO = mapperUpdateEmployee
                .EmployeeDTOToUpdateEmployeeDTO(employeeDTO);
        mapperUpdateEmployee.updateEmployeeDTO(updateData, updateEmployeeDTO);
        employeeDTO = mapperUpdateEmployee.updateEmployeeDTOToEmployeeDTO(updateEmployeeDTO);
        saveEmployee(employeeDTO);
    }

    @Override
    public void addEmployeeInDepartment(EmployeeDTO employeeDTO) {
        DepartmentDTO departmentDTO = departmentService.getAllDepartments().stream()
                .filter(departmentDto -> departmentDto.getName().equals(employeeDTO.getDepartmentName()))
                .findFirst().orElse(null);
    }

    @Override
    @Transactional
    public void deleteEmployee(UUID id){
        emploeeyRepository.deleteById(id);
    }
}