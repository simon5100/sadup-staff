package sadupstaff.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sadupstaff.dto.management.department.UpdateDepartmentDTO;
import sadupstaff.mapper.management.department.MapperCreateDepartment;
import sadupstaff.mapper.management.department.MapperFindDepartment;
import sadupstaff.mapper.management.department.MapperUpdateDepartment;
import sadupstaff.model.department.CreateRequestDepartment;
import sadupstaff.model.department.ResponseDepartment;
import sadupstaff.model.department.UpdateRequestDepartment;
import sadupstaff.service.department.DepartmentService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DepartmentRESTController {

    private final DepartmentService departmentService;
    private final MapperCreateDepartment mapperCreateDepartment;
    private final MapperUpdateDepartment mapperUpdateDepartment;
    private final MapperFindDepartment mapperFindDepartment;

    @GetMapping("/v1/departments")
    public List<ResponseDepartment> showAllDepartments() {
        return departmentService.getAllDepartments().stream()
                .map(departmentDTO -> mapperFindDepartment.DTOToResponseDepartment(departmentDTO)).
                collect(Collectors.toList());
    }

    @GetMapping("/v1/departments/{id}")
    public ResponseDepartment getDepartment(@PathVariable UUID id) {
        return mapperFindDepartment.DTOToResponseDepartment(departmentService.getDepartmentForId(id));
    }

    @PostMapping("/v1/departments")
    public ResponseDepartment addDepartment(@RequestBody CreateRequestDepartment createRequest) {
        return getDepartment(departmentService.saveDepartment(mapperCreateDepartment.createDepartmentToDTO(createRequest)));
    }

    @PutMapping("/v1/departments/{id}")
    public ResponseDepartment updateDepartment(@PathVariable UUID id, @RequestBody UpdateRequestDepartment updateRequest) {
        UpdateDepartmentDTO updateData = mapperUpdateDepartment.updateRequestToDTO(updateRequest);
        departmentService.updateDepartment(id, updateData);
        return getDepartment(id);
    }

    @DeleteMapping("/v1/departments/{id}")
    public void deleteDepartment(@PathVariable UUID id) {
        departmentService.deleteDepartment(id);
    }
}