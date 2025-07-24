package sadupstaff.service.department;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sadupstaff.dto.management.department.DepartmentDTO;
import sadupstaff.dto.management.department.UpdateDepartmentDTO;
import sadupstaff.mapper.management.department.MapperCreateDepartment;
import sadupstaff.mapper.management.department.MapperFindDepartment;
import sadupstaff.mapper.management.department.MapperUpdateDepartment;
import sadupstaff.repository.DepartmentRepository;
import sadupstaff.entity.management.Department;
import sadupstaff.service.UpdatingData;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final UpdatingData updatingData;
    private final MapperCreateDepartment mapperCreateDepartment;
    private final MapperUpdateDepartment mapperUpdateDepartment;
    private final MapperFindDepartment mapperFindDepartment;

    @Override
    @Transactional
    public List<DepartmentDTO> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(department -> mapperFindDepartment.departmentToDepartmentDTO(department))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DepartmentDTO getDepartmentForId(UUID id) {
        Optional<Department> departmentOptional = departmentRepository.findById(id);
        if (departmentOptional.isPresent()) {
            return mapperFindDepartment.departmentToDepartmentDTO(departmentOptional.get());
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Department getDepartmentForName(String name) {
        Optional<Department> departmentOptional = Optional.ofNullable(departmentRepository.findDepartmentByName(name));
        if (departmentOptional.isPresent()) {
            return departmentOptional.get();
        }
        return null;
    }

    @Override
    @Transactional
    public UUID saveDepartment(DepartmentDTO departmentDTO) {
        Department department = mapperCreateDepartment.DTOToEntity(departmentDTO);
        if (department.getCreatedAt() == null) {
            department.setCreatedAt(LocalDateTime.now());
        }
        department.setUpdatedAt(LocalDateTime.now());
        return departmentRepository.save(department).getId();
    }

    @Override
    @Transactional
    public void updateDepartment(UUID id, UpdateDepartmentDTO updateData) {
        DepartmentDTO departmentDTO = getDepartmentForId(id);
        UpdateDepartmentDTO updateDepartmentDTO = mapperUpdateDepartment
                .departmentDTOToUpdateDepartmentDTO(departmentDTO);
        mapperUpdateDepartment.updateDepartmentDTO(updateData, updateDepartmentDTO);
        departmentDTO = mapperUpdateDepartment.updateDepartmentDTOToDepartmentDTO(updateDepartmentDTO);
        saveDepartment(departmentDTO);
    }

    @Override
    @Transactional
    public void deleteDepartment(UUID id){
        departmentRepository.deleteById(id);
    }
}
