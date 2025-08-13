package sadupstaff.service.department;

import sadupstaff.dto.request.department.CreateDepartmentRequest;
import sadupstaff.dto.request.department.UpdateDepartmentRequest;
import sadupstaff.dto.response.DepartmentResponse;
import sadupstaff.entity.management.Department;
import java.util.List;
import java.util.UUID;

public interface DepartmentService {
    List<DepartmentResponse> getAllDepartments();

    DepartmentResponse getDepartmentById(UUID id);

    Department getDepartmentByIdForUpdate(UUID id);

    Department getDepartmentByName(String name);

    DepartmentResponse saveDepartment(CreateDepartmentRequest createRequest);

    DepartmentResponse updateDepartment(UUID id, UpdateDepartmentRequest updateRequest);

    void deleteDepartment(UUID id);
}
