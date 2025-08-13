package sadupstaff.service.department;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sadupstaff.dto.department.UpdateDepartmentDTO;
import sadupstaff.dto.request.department.CreateDepartmentRequest;
import sadupstaff.dto.request.department.UpdateDepartmentRequest;
import sadupstaff.dto.response.DepartmentResponse;
import sadupstaff.mapper.department.MapperCreateDepartment;
import sadupstaff.mapper.department.MapperFindDepartment;
import sadupstaff.mapper.department.MapperUpdateDepartment;
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
    private final MapperUpdateDepartment mapperUpdateDepartment;
    private final MapperFindDepartment mapperFindDepartment;
    private final MapperCreateDepartment mapperCreateDepartment;

    @Override
    @Transactional
    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(department -> mapperFindDepartment.entityToResponse(department))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DepartmentResponse getDepartmentById(UUID id) {
        Optional<Department> departmentOptional = departmentRepository.findById(id);
        if (departmentOptional.isPresent()) {
            return mapperFindDepartment.entityToResponse(departmentOptional.get());
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
        Department department = mapperCreateDepartment.toEntity(createRequest);
        department.setCreatedAt(LocalDateTime.now());
        department.setUpdatedAt(LocalDateTime.now());
        department = departmentRepository.save(department);

        return getDepartmentById(department.getId());
    }

    @Override
    @Transactional
    public DepartmentResponse updateDepartment(UUID id, UpdateDepartmentRequest updateRequest) {
        UpdateDepartmentDTO updateData = mapperUpdateDepartment.updateRequestToDTO(updateRequest);
        UpdateDepartmentDTO updateDepartmentOld =
                mapperUpdateDepartment.departmentToUpdateDepartmentDTO(getDepartmentByIdForUpdate(id));
        mapperUpdateDepartment.updateDepartmentData(updateData, updateDepartmentOld);
        Department department = mapperUpdateDepartment.updateDepartmentDTOToDepartment(updateDepartmentOld);
        department.setUpdatedAt(LocalDateTime.now());
        departmentRepository.save(department);

        return getDepartmentById(id);
    }

    @Override
    @Transactional
    public void deleteDepartment(UUID id){
        departmentRepository.deleteById(id);
    }
}
