package sadupstaff.service.department;

import sadupstaff.dto.management.department.DepartmentDTO;
import sadupstaff.dto.management.department.UpdateDepartmentDTO;
import sadupstaff.entity.management.Department;
import java.util.List;
import java.util.UUID;

public interface DepartmentService {
    public List<DepartmentDTO> getAllDepartments();

    public DepartmentDTO getDepartmentById(UUID id);

    public Department getDepartmentByName(String name);

    public UUID saveDepartment(DepartmentDTO departmentDTO);

    public void updateDepartment(UUID id, UpdateDepartmentDTO updateData);

    public void deleteDepartment(UUID id);
}
