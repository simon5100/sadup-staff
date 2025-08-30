package sadupstaff.service.department;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sadupstaff.dto.request.create.CreateDepartmentRequest;
import sadupstaff.dto.request.update.UpdateDepartmentRequest;
import sadupstaff.dto.response.DepartmentResponse;
import sadupstaff.enums.DepartmentNameEnum;
import sadupstaff.exception.PositionOccupiedException;
import sadupstaff.exception.department.DeleteDepartmentException;
import sadupstaff.exception.DepartmentNotFoundException;
import sadupstaff.exception.IdNotFoundException;
import sadupstaff.mapper.department.CreateDepartmentMapper;
import sadupstaff.mapper.department.FindDepartmentMapper;
import sadupstaff.mapper.department.UpdateDepartmentMapper;
import sadupstaff.repository.DepartmentRepository;
import sadupstaff.entity.management.Department;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
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
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(id.toString()));

        return findDepartmentMapper.entityToResponse(department);
    }

    @Override
    public Department getDepartmentByName(DepartmentNameEnum name) {
        return Arrays.stream(DepartmentNameEnum.values())
                .filter(value -> name.equals(value))
                .findFirst()
                .map(value -> departmentRepository.findDepartmentByName(value))
                .orElseThrow(() -> new DepartmentNotFoundException(name.getStringConvert()));
    }

    @Override
    @Transactional
    public DepartmentResponse saveDepartment(CreateDepartmentRequest createRequest) {

        if (departmentRepository.existsDistinctByName(createRequest.getName())) {
            throw new PositionOccupiedException(createRequest.getName().getStringConvert());
        }

        Department department = createDepartmentMapper.toEntity(createRequest);

        department.setCreatedAt(LocalDateTime.now());
        department.setUpdatedAt(LocalDateTime.now());
        department = departmentRepository.save(department);

        return getDepartmentById(department.getId());
    }

    @Override
    @Transactional
    public DepartmentResponse updateDepartment(UUID id, UpdateDepartmentRequest updateData) {
        Department departmentOld = departmentRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(id.toString()));

        if (updateData.getName() != null && departmentRepository.existsDistinctByName(updateData.getName())) {
            throw new PositionOccupiedException(updateData.getName().getStringConvert());
        }

        updateDepartmentMapper.updateDepartmentData(updateData, departmentOld);

        departmentOld.setUpdatedAt(LocalDateTime.now());
        departmentRepository.save(departmentOld);

        return findDepartmentMapper.entityToResponse(departmentOld);
    }

    @Override
    @Transactional
    public void deleteDepartment(UUID id){
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(id.toString()));
        if (!department.getEmps().isEmpty()) {
            throw new DeleteDepartmentException(department.getName().getStringConvert());
        }

        departmentRepository.deleteById(id);
    }
}
