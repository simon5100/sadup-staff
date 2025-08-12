package sadupstaff.controller.department;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sadupstaff.dto.request.department.CreateRequestDepartment;
import sadupstaff.dto.response.ResponseDepartment;
import sadupstaff.dto.request.department.UpdateRequestDepartment;
import sadupstaff.service.department.DepartmentService;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class DepartmentRESTControllerImpl implements DepartmentRESTController {

    private final DepartmentService departmentService;

    public List<ResponseDepartment> showAllDepartments() {
        return departmentService.getAllDepartments();
    }

    public ResponseDepartment getDepartment(@PathVariable UUID id) {
        return departmentService.getDepartmentById(id);
    }

    public ResponseDepartment addDepartment(@RequestBody CreateRequestDepartment createRequest) {
        return departmentService.saveNewDepartment(createRequest);
    }

    public ResponseDepartment updateDepartment(@PathVariable UUID id, @RequestBody UpdateRequestDepartment updateRequest) {
        return departmentService.updateDepartment(id, updateRequest);
    }

    public void deleteDepartment(@PathVariable UUID id) {
        departmentService.deleteDepartment(id);
    }
}