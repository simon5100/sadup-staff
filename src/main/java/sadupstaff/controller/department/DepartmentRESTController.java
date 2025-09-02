package sadupstaff.controller.department;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import sadupstaff.dto.request.create.CreateDepartmentRequest;
import sadupstaff.dto.request.update.UpdateDepartmentRequest;
import sadupstaff.dto.response.DepartmentResponse;
import java.util.List;
import java.util.UUID;

@Tag(name = "Department API", description = "API взаимодействующий с Department")
@RequestMapping("/api")
public interface DepartmentRESTController {

    @Operation(summary = "Вызов всего списка отделов")
    @GetMapping("/v1/departments")
    List<DepartmentResponse> showAllDepartments();

    @Operation(summary = "Вызов отдела по id")
    @GetMapping("/v1/departments/{id}")
    DepartmentResponse getDepartment(@PathVariable UUID id);

    @Operation(summary = "Создание нового отдела")
    @PostMapping("/v1/departments")
    DepartmentResponse addDepartment(@RequestBody CreateDepartmentRequest createRequest);

    @Operation(summary = "Изменение параметров отдела")
    @PutMapping("/v1/departments/{id}")
    DepartmentResponse updateDepartment(@PathVariable UUID id, @RequestBody UpdateDepartmentRequest updateRequest);

    @Operation(summary = "Удаление отдела id")
    @DeleteMapping("/v1/departments/{id}")
    void deleteDepartment(@PathVariable UUID id);
}