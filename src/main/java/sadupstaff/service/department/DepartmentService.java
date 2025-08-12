package sadupstaff.service.department;

import sadupstaff.dto.department.DepartmentDTO;
import sadupstaff.dto.request.department.CreateRequestDepartment;
import sadupstaff.dto.request.department.UpdateRequestDepartment;
import sadupstaff.dto.response.ResponseDepartment;
import sadupstaff.entity.management.Department;
import java.util.List;
import java.util.UUID;

public interface DepartmentService {
    List<ResponseDepartment> getAllDepartments();

    ResponseDepartment getDepartmentById(UUID id);

    DepartmentDTO getDepartmentByIdForUpdate(UUID id);

    Department getDepartmentByName(String name);

    ResponseDepartment saveNewDepartment(CreateRequestDepartment createRequest);

    ResponseDepartment updateDepartment(UUID id, UpdateRequestDepartment updateRequest);

    void deleteDepartment(UUID id);
}
