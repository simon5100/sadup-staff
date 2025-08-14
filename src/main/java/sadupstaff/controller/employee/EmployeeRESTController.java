package sadupstaff.controller.employee;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import sadupstaff.dto.request.create.CreateEmployeeRequest;
import sadupstaff.dto.request.update.UpdateEmployeeRequest;
import sadupstaff.dto.response.EmployeeResponse;
import java.util.List;
import java.util.UUID;

@Tag(name = "Employee API", description = "API взаимодействующий с Employee")
@RequestMapping("/api")
public interface EmployeeRESTController {

    @Operation(summary = "Вызов всего списка сотрудников")
    @GetMapping("/v1/employees")
    List<EmployeeResponse> showAllEmployees();

    @Operation(summary = "Вызов сотрудника по id")
    @GetMapping("/v1/employees/{id}")
    EmployeeResponse getEmployee(@PathVariable UUID id);

    @Operation(summary = "Создание нового сотрудника")
    @PostMapping("/v1/employees")
    EmployeeResponse addEmployee(@RequestBody CreateEmployeeRequest createEmployeeRequest);

    @Operation(summary = "Изменение параметров сотрудника")
    @PutMapping("/v1/employees/{id}")
    EmployeeResponse updateEmployee(@PathVariable UUID id, @RequestBody UpdateEmployeeRequest updateEmployeeRequest);

    @Operation(summary = "Удаление сотрудника по id")
    @DeleteMapping("/v1/employees/{id}")
    void deleteEmployee(@PathVariable UUID id);
}