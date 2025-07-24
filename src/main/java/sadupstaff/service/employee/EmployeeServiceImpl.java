package sadupstaff.service.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.Mergeable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sadupstaff.dto.management.department.DepartmentDTO;
import sadupstaff.dto.management.department.UpdateDepartmentDTO;
import sadupstaff.dto.management.employee.EmployeeDTO;
import sadupstaff.dto.management.employee.UpdateEmployeeDTO;
import sadupstaff.entity.management.Department;
import sadupstaff.mapper.management.department.MapperCreateDepartment;
import sadupstaff.mapper.management.department.MapperDepartment;
import sadupstaff.mapper.management.department.MapperFindDepartment;
import sadupstaff.mapper.management.department.MapperUpdateDepartment;
import sadupstaff.mapper.management.employee.MapperCreateEmployee;
import sadupstaff.mapper.management.employee.MapperEmployee;
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
    private final MapperUpdateDepartment mapperUpdateDepartment;
    private final MapperDepartment mapperDepartment;
    private final MapperEmployee mapperEmployee;

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
        Employee employee = mapperEmployee.toEmployee(employeeDTO);
        Department department = departmentService.getDepartmentForName(employeeDTO.getDepartmentName());
        employee.setDepartment(department);
        if (employee.getCreatedAt() == null) {
            employee.setCreatedAt(LocalDateTime.now());
        }
        employee.setUpdatedAt(LocalDateTime.now());
        employee = emploeeyRepository.save(employee);
        department.addEmployee(employee);
        return employee.getId();
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
    @Transactional
    public Employee addEmployeeInDepartment(EmployeeDTO employeeDTO) {
        Employee employee = mapperCreateEmployee.toEntity(employeeDTO);
        DepartmentDTO departmentDTO = departmentService.getAllDepartments().stream()
                .filter(departmentDto -> departmentDto.getName().equalsIgnoreCase(employeeDTO.getDepartmentName()))
                .findFirst().orElse(null);
        Department department = mapperDepartment.toDepartment(departmentDTO);
        department.addEmployee(employee);
        departmentDTO = mapperDepartment.toDTO(department);
        UpdateDepartmentDTO updateDepartmentDTO = mapperUpdateDepartment.departmentDTOToUpdateDepartmentDTO(departmentDTO);
        departmentService.updateDepartment(departmentDTO.getId(), updateDepartmentDTO);
        return employee;
    }

    @Override
    @Transactional
    public void deleteEmployee(UUID id){
        emploeeyRepository.deleteById(id);
    }
}