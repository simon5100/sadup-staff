package sadupstaff.service.department;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sadupstaff.dto.request.create.CreateDepartmentRequest;
import sadupstaff.dto.request.update.UpdateDepartmentRequest;
import sadupstaff.dto.response.DepartmentResponse;
import sadupstaff.mapper.department.CreateDepartmentMapper;
import sadupstaff.mapper.department.FindDepartmentMapper;
import sadupstaff.mapper.department.UpdateDepartmentMapper;
import sadupstaff.repository.DepartmentRepository;
import sadupstaff.entity.management.Department;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final UpdateDepartmentMapper updateDepartmentMapper;
    private final FindDepartmentMapper findDepartmentMapper;
    private final CreateDepartmentMapper createDepartmentMapper;

    @Override
    @Transactional
    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(department -> findDepartmentMapper.entityToResponse(department))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DepartmentResponse getDepartmentById(UUID id) {
        Optional<Department> departmentOptional = departmentRepository.findById(id);
        if (departmentOptional.isPresent()) {
            return findDepartmentMapper.entityToResponse(departmentOptional.get());
        }
        return null;
    }

    @Override
    public Department getDepartmentByIdForUpdate(UUID id) {
        Optional<Department> departmentOptional = departmentRepository.findById(id);
        if (departmentOptional.isPresent()) {
            return departmentOptional.get();
        }
        return null;
    }

    @Override
    public Department getDepartmentByName(String name) {
        Optional<Department> departmentOptional = Optional.ofNullable(departmentRepository.findDepartmentByName(name));
        if (departmentOptional.isPresent()) {
            return departmentOptional.get();
        }
        return null;
    }

    @Override
    @Transactional
    public DepartmentResponse saveDepartment(CreateDepartmentRequest createRequest) {
        Department department = createDepartmentMapper.toEntity(createRequest);
        department.setCreatedAt(LocalDateTime.now());
        department.setUpdatedAt(LocalDateTime.now());
        department = departmentRepository.save(department);

        return getDepartmentById(department.getId());
    }

    @Override
    @Transactional
    public DepartmentResponse updateDepartment(UUID id, UpdateDepartmentRequest updateData) {
        Department departmentOld = getDepartmentByIdForUpdate(id);
        updateDepartmentMapper.updateDepartmentData(updateData, departmentOld);
        departmentOld.setUpdatedAt(LocalDateTime.now());
        departmentRepository.save(departmentOld);

        return findDepartmentMapper.entityToResponse(departmentOld);
    }

    @Override
    @Transactional
    public void deleteDepartment(UUID id){
        departmentRepository.deleteById(id);
    }
}
