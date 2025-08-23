package sadupstaff.service.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sadupstaff.dto.request.create.CreateEmployeeRequest;
import sadupstaff.dto.request.update.UpdateEmployeeRequest;
import sadupstaff.dto.response.EmployeeResponse;
import sadupstaff.entity.management.Department;
import sadupstaff.exception.IdNotFoundException;
import sadupstaff.exception.employee.MaxEmployeeInDepartmentException;
import sadupstaff.exception.PositionOccupiedException;
import sadupstaff.mapper.employee.CreateEmployeeMapper;
import sadupstaff.mapper.employee.FindEmployeeMapper;
import sadupstaff.mapper.employee.UpdateEmployeeMapper;
import sadupstaff.entity.management.Employee;
import sadupstaff.repository.EmployeeRepository;
import sadupstaff.service.department.DepartmentServiceImpl;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentServiceImpl departmentService;
    private final UpdateEmployeeMapper updateEmployeeMapper;
    private final FindEmployeeMapper findEmployeeMapper;
    private final CreateEmployeeMapper createEmployeeMapper;

    @Override
    @Transactional
    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(employee -> findEmployeeMapper.entityToResponse(employee))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmployeeResponse getEmployee(UUID id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(id.toString()));

        return findEmployeeMapper.entityToResponse(employee);
    }

    @Override
    @Transactional
    public EmployeeResponse saveEmployee(CreateEmployeeRequest createEmployeeRequest) {
        Employee employee = createEmployeeMapper.toEntity(createEmployeeRequest);
        Department department = departmentService.getDepartmentByName(createEmployeeRequest.getDepartmentName());

        if (department.getMaxNumberEmployees() == department.getEmps().size()) {
            throw new MaxEmployeeInDepartmentException(createEmployeeRequest.getDepartmentName().getStringConvert());
        }

        for (Employee emp: department.getEmps()) {
            if (emp.getPosition().equals(employee.getPosition())) {
                throw new PositionOccupiedException(createEmployeeRequest.getPosition().getStringConvert());
            }
        }

        employee.setDepartment(department);
        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());
        employee = employeeRepository.save(employee);

        return getEmployee(employee.getId());
    }

    @Override
    @Transactional
    public EmployeeResponse updateEmployee(UUID id, UpdateEmployeeRequest updateData) {
        Employee employeeOld = employeeRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(id.toString()));

        if (updateData.getPosition() != null &&  employeeRepository.existsEmployeeByPosition(updateData.getPosition())) {
            throw new PositionOccupiedException(updateData.getPosition().getStringConvert());
        }

        updateEmployeeMapper.updateEmployeeData(updateData, employeeOld);
        employeeOld.setUpdatedAt(LocalDateTime.now());
        employeeRepository.save(employeeOld);

        return findEmployeeMapper.entityToResponse(employeeOld);
    }

    @Override
    @Transactional
    public void deleteEmployee(UUID id){
        employeeRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(id.toString()));

        employeeRepository.deleteById(id);
    }
}