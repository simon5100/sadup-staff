package sadupstaff.service.department;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sadupstaff.dto.department.DepartmentDTO;
import sadupstaff.dto.department.UpdateDepartmentDTO;
import sadupstaff.dto.request.department.CreateRequestDepartment;
import sadupstaff.dto.request.department.UpdateRequestDepartment;
import sadupstaff.dto.response.ResponseDepartment;
import sadupstaff.mapper.department.MapperCreateDepartment;
import sadupstaff.mapper.department.MapperDepartment;
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
    private final MapperDepartment mapperDepartment;
    private final MapperFindDepartment mapperFindDepartment;
    private final MapperCreateDepartment mapperCreateDepartment;

    @Override
    @Transactional
    public List<ResponseDepartment> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(department -> mapperFindDepartment.entityToResponseDepartment(department))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ResponseDepartment getDepartmentById(UUID id) {
        Optional<Department> departmentOptional = departmentRepository.findById(id);
        if (departmentOptional.isPresent()) {
            return mapperFindDepartment.entityToResponseDepartment(departmentOptional.get());
        }
        return null;
    }

    @Override
    public DepartmentDTO getDepartmentByIdForUpdate(UUID id) {
        Optional<Department> departmentOptional = departmentRepository.findById(id);
        if (departmentOptional.isPresent()) {
            return mapperDepartment.toDTO(departmentOptional.get());
        }
        return null;
    }

    @Override
    @Transactional
    public Department getDepartmentByName(String name) {
        Optional<Department> departmentOptional = Optional.ofNullable(departmentRepository.findDepartmentByName(name));
        if (departmentOptional.isPresent()) {
            return departmentOptional.get();
        }
        return null;
    }

    @Override
    @Transactional
    public ResponseDepartment saveNewDepartment(CreateRequestDepartment createRequest) {
        Department department = mapperCreateDepartment.createDepartmentToEntity(createRequest);
        department.setCreatedAt(LocalDateTime.now());
        department.setUpdatedAt(LocalDateTime.now());
        department = departmentRepository.save(department);

        return getDepartmentById(department.getId());
    }

    @Override
    @Transactional
    public ResponseDepartment updateDepartment(UUID id, UpdateRequestDepartment updateRequest) {
        UpdateDepartmentDTO updateData = mapperUpdateDepartment.updateRequestToDTO(updateRequest);
        DepartmentDTO departmentDTO = getDepartmentByIdForUpdate(id);
        UpdateDepartmentDTO updateDepartmentDTO = mapperUpdateDepartment
                .departmentDTOToUpdateDepartmentDTO(departmentDTO);
        mapperUpdateDepartment.updateDepartmentDTO(updateData, updateDepartmentDTO);
        departmentDTO = mapperUpdateDepartment.updateDepartmentDTOToDepartmentDTO(updateDepartmentDTO);
        Department department = mapperDepartment.toDepartment(departmentDTO);
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
