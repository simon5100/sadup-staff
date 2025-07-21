package sadupstaff.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sadupstaff.entity.management.Department;
import sadupstaff.service.department.DepartmentService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DepartmentRESTController {

    private final DepartmentService departmentService;

    @GetMapping("/v1/departments")
    public List<Department> showAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @GetMapping("/v1/departments/{id}")
    public Department getDepartment(@PathVariable UUID id) {
        Department department = departmentService.getDepartment(id);
        return department;
    }

    @PostMapping("/v1/departments")
    public Department addDepartment(@RequestBody Department department) {
        departmentService.saveDepartment(department);
        return department;
    }

    @PutMapping("/v1/departments/{id}")
    public Department updateDepartment(@PathVariable UUID id, @RequestBody Department department) {
        departmentService.updateDepartment(id, department);
        return departmentService.getDepartment(id);
    }

    @DeleteMapping("/v1/departments/{id}")
    public void deleteDepartment(@PathVariable UUID id) {
        departmentService.deleteDepartment(id);
    }
}