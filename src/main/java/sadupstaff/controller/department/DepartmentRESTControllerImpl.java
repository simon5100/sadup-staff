package sadupstaff.controller.department;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sadupstaff.dto.management.department.UpdateDepartmentDTO;
import sadupstaff.mapper.department.MapperCreateDepartment;
import sadupstaff.mapper.department.MapperFindDepartment;
import sadupstaff.mapper.department.MapperUpdateDepartment;
import sadupstaff.model.department.CreateRequestDepartment;
import sadupstaff.model.department.ResponseDepartment;
import sadupstaff.model.department.UpdateRequestDepartment;
import sadupstaff.service.department.DepartmentService;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class DepartmentRESTControllerImpl implements DepartmentRESTController {

    private final DepartmentService departmentService;
    private final MapperCreateDepartment mapperCreateDepartment;
    private final MapperUpdateDepartment mapperUpdateDepartment;
    private final MapperFindDepartment mapperFindDepartment;

    public List<ResponseDepartment> showAllDepartments() {
        return departmentService.getAllDepartments().stream()
                .map(departmentDTO -> mapperFindDepartment.DTOToResponseDepartment(departmentDTO)).
                collect(Collectors.toList());
    }

    public ResponseDepartment getDepartment(@PathVariable UUID id) {
        return mapperFindDepartment.DTOToResponseDepartment(departmentService.getDepartmentById(id));
    }

    public ResponseDepartment addDepartment(@RequestBody CreateRequestDepartment createRequest) {
        return getDepartment(departmentService.saveDepartment(mapperCreateDepartment.createDepartmentToDTO(createRequest)));
    }

    public ResponseDepartment updateDepartment(@PathVariable UUID id, @RequestBody UpdateRequestDepartment updateRequest) {
        UpdateDepartmentDTO updateData = mapperUpdateDepartment.updateRequestToDTO(updateRequest);
        departmentService.updateDepartment(id, updateData);
        return getDepartment(id);
    }

    public void deleteDepartment(@PathVariable UUID id) {
        departmentService.deleteDepartment(id);
    }
}