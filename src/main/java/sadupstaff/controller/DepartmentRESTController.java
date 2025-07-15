package sadupstaff.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sadupstaff.entity.management.Department;
import sadupstaff.service.department_servic.DepartmentService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DepartmentRESTController {


    private final DepartmentService departmentService;

    @GetMapping("/departments")
    public List<Department> showAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @GetMapping("/departments/{id}")
    public Department getDepartment(@PathVariable UUID id) {
        Department department = departmentService.getDepartment(id);

        return department;
    }

    @PostMapping("/departments")
    public Department addDepartment(@RequestBody Department department) {
        departmentService.saveDepartment(department);
        return department;
    }

    @PutMapping("/departments")
    public Department updateDepartment(@RequestBody Department department) {
        departmentService.saveDepartment(department);
        return department;
    }

    @DeleteMapping("/departments/{id}")
    public String deleteDepartment(@PathVariable UUID id) {
        Department department = departmentService.getDepartment(id);

        departmentService.deleteDepartment(id);

        return department + " удален";
    }

}
