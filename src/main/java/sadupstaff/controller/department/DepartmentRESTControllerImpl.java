package sadupstaff.controller.department;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sadupstaff.dto.request.department.CreateDepartmentRequest;
import sadupstaff.dto.response.DepartmentResponse;
import sadupstaff.dto.request.department.UpdateDepartmentRequest;
import sadupstaff.service.department.DepartmentService;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class DepartmentRESTControllerImpl implements DepartmentRESTController {

    private final DepartmentService departmentService;

    public List<DepartmentResponse> showAllDepartments() {
        return departmentService.getAllDepartments();
    }

    public DepartmentResponse getDepartment(@PathVariable UUID id) {
        return departmentService.getDepartmentById(id);
    }

    public DepartmentResponse addDepartment(@RequestBody CreateDepartmentRequest createRequest) {
        return departmentService.saveDepartment(createRequest);
    }

    public DepartmentResponse updateDepartment(@PathVariable UUID id, @RequestBody UpdateDepartmentRequest updateRequest) {
        return departmentService.updateDepartment(id, updateRequest);
    }

    public void deleteDepartment(@PathVariable UUID id) {
        departmentService.deleteDepartment(id);
    }
}