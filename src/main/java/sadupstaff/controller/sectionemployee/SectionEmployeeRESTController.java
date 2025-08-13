package sadupstaff.controller.sectionemployee;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import sadupstaff.dto.request.sectionemployee.CreateSectionEmployeeRequest;
import sadupstaff.dto.request.sectionemployee.UpdateSectionEmployeeRequest;
import sadupstaff.dto.response.SectionEmployeeResponse;

import java.util.List;
import java.util.UUID;

@Tag(name = "SectionEmployee API", description = "API взаимодействующий с SectionEmployee")
@RequestMapping("/api")
public interface SectionEmployeeRESTController {

    @Operation(summary = "Вызов всего списка сотрудников")
    @GetMapping("/v1/sectionEmployees")
    List<SectionEmployeeResponse> showSectionEmployees();

    @Operation(summary = "Вызов сотрудника по id")
    @GetMapping("/v1/sectionEmployees/{id}")
    SectionEmployeeResponse showSectionEmployee(@PathVariable UUID id);

    @Operation(summary = "Создание нового сотрудника")
    @PostMapping("/v1/sectionEmployees")
    SectionEmployeeResponse addSectionEmployee (@RequestBody CreateSectionEmployeeRequest createRequest);

    @Operation(summary = "Изменение параметров сотрудника")
    @PutMapping("/v1/sectionEmployees/{id}")
    SectionEmployeeResponse updateSectionEmployee(@PathVariable UUID id, @RequestBody UpdateSectionEmployeeRequest updateRequest);

    @Operation(summary = "Удаление сотрудника по id")
    @DeleteMapping("/v1/sectionEmployees/{id}")
    void deleteSection(@PathVariable UUID id);
}