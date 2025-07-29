package sadupstaff.controller.employee;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import sadupstaff.model.employee.CreateRequestEmployee;
import sadupstaff.model.employee.ResponseEmployee;
import sadupstaff.model.employee.UpdateRequestEmployee;
import java.util.List;
import java.util.UUID;

@Tag(name = "Employee API", description = "Класс взаимодействующий с Employee")
@RequestMapping("/api")
public interface EmployeeRESTController {

    @Operation(summary = "Вызов всего списка сотрудников")
    @GetMapping("/v1/employees")
    List<ResponseEmployee> showAllEmployees();

    @Operation(summary = "Вызов сотрудника по id")
    @GetMapping("/v1/employees/{id}")
    ResponseEmployee getEmployee(@PathVariable UUID id);

    @Operation(summary = "Создание нового сотрудника и добавление его в базу данных")
    @PostMapping("/v1/employees")
    ResponseEmployee addEmployee(@RequestBody CreateRequestEmployee createRequestEmployee);

    @Operation(summary = "Изменение параметров сотрудника")
    @PutMapping("/v1/employees/{id}")
    ResponseEmployee updateEmployee(@PathVariable UUID id, @RequestBody UpdateRequestEmployee updateRequestEmployee);

    @Operation(summary = "Удаление сотрудника из базы данных по id")
    @DeleteMapping("/v1/employees/{id}")
    void deleteEmployee(@PathVariable UUID id);
}