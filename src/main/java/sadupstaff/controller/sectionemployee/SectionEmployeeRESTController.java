package sadupstaff.controller.sectionemployee;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import sadupstaff.dto.request.sectionemployee.CreateRequestSectionEmployee;
import sadupstaff.dto.response.ResponseSectionEmployee;
import sadupstaff.dto.request.sectionemployee.UpdateRequestSectionEmployee;
import java.util.List;
import java.util.UUID;

@Tag(name = "SectionEmployee API", description = "Класс взаимодействующий с SectionEmployee")
@RequestMapping("/api")
public interface SectionEmployeeRESTController {

    @Operation(summary = "Вызов всего списка сотрудников")
    @GetMapping("/v1/sectionEmployees")
    List<ResponseSectionEmployee> showSectionEmployees();

    @Operation(summary = "Вызов сотрудника по id")
    @GetMapping("/v1/sectionEmployees/{id}")
    ResponseSectionEmployee showSectionEmployee(@PathVariable UUID id);

    @Operation(summary = "Создание нового сотрудника и добавление его в базу данных")
    @PostMapping("/v1/sectionEmployees")
    ResponseSectionEmployee addSectionEmployee (@RequestBody CreateRequestSectionEmployee createRequest);

    @Operation(summary = "Изменение параметров сотрудника")
    @PutMapping("/v1/sectionEmployees/{id}")
    ResponseSectionEmployee updateSectionEmployee(@PathVariable UUID id, @RequestBody UpdateRequestSectionEmployee updateRequest);

    @Operation(summary = "Удаление сотрудника из базы данных по id")
    @DeleteMapping("/v1/sectionEmployees/{id}")
    void deleteSection(@PathVariable UUID id);
}