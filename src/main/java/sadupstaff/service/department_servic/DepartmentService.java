package sadupstaff.service.department_servic;

import sadupstaff.entity.management.Department;
import java.util.List;
import java.util.UUID;

public interface DepartmentService {
    public List<Department> getAllDepartments();

    public Department getDepartment(UUID id);

    public void saveDepartment(Department department);

    public void updateDepartment(UUID id, Department departmentNew);

    public void deleteDepartment(UUID id);
}
