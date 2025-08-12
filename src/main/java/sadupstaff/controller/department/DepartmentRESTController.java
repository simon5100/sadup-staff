package sadupstaff.controller.department;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import sadupstaff.dto.request.department.CreateRequestDepartment;
import sadupstaff.dto.response.ResponseDepartment;
import sadupstaff.dto.request.department.UpdateRequestDepartment;
import java.util.List;
import java.util.UUID;

@Tag(name = "Department API", description = "Класс взаимодействующий с Department")
@RequestMapping("/api")
public interface DepartmentRESTController {

    @Operation(summary = "Вызов всего списка отделов")
    @GetMapping("/v1/departments")
    List<ResponseDepartment> showAllDepartments();

    @Operation(summary = "Вызов отдела по id")
    @GetMapping("/v1/departments/{id}")
    ResponseDepartment getDepartment(@PathVariable UUID id);

    @Operation(summary = "Создание нового отдела и добавление его в базу данных")
    @PostMapping("/v1/departments")
    ResponseDepartment addDepartment(@RequestBody CreateRequestDepartment createRequest);

    @Operation(summary = "Изменение параметров отдела")
    @PutMapping("/v1/departments/{id}")
    ResponseDepartment updateDepartment(@PathVariable UUID id, @RequestBody UpdateRequestDepartment updateRequest);

    @Operation(summary = "Удаление отдела из базы данных по id")
    @DeleteMapping("/v1/departments/{id}")
    void deleteDepartment(@PathVariable UUID id);
}