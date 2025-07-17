package sadupstaff.service.department_servic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sadupstaff.repository.DepartmentRepository;
import sadupstaff.entity.management.Department;
import sadupstaff.service.UpdatingData;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    private final UpdatingData updatingData;

    @Override
    @Transactional
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    @Transactional
    public Department getDepartment(UUID id) {
        Optional<Department> departmentOptional = departmentRepository.findById(id);
        if (departmentOptional.isPresent()) {
            return departmentOptional.get();
        }
        return null;
    }

    @Override
    @Transactional
    public void saveDepartment(Department department) {
        departmentRepository.save(department);
    }

    @Override
    @Transactional
    public void updateDepartment(UUID id, Department departmentUpdate) {
        Department department = getDepartment(id);
        departmentRepository.save(updatingData.updatingData(department, departmentUpdate, Department.class));
    }

    @Override
    @Transactional
    public void deleteDepartment(UUID id){
        departmentRepository.deleteById(id);
    }
}
