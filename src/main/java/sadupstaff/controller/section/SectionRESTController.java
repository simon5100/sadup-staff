package sadupstaff.controller.section;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import sadupstaff.model.section.CreateRequestSection;
import sadupstaff.model.section.ResponseSection;
import sadupstaff.model.section.UpdateRequestSection;
import java.util.List;
import java.util.UUID;

@Tag(name = "Section API", description = "Класс взаимодействующий с Section")
@RequestMapping("/api")
public interface SectionRESTController {

    @Operation(summary = "Вызов всего списка участков")
    @GetMapping("/v1/sections")
    List<ResponseSection> showSections();

    @Operation(summary = "Вызов участка по id")
    @GetMapping("/v1/sections/{id}")
    ResponseSection showSection(@PathVariable UUID id);

    @Operation(summary = "Создание нового участка и добавление его в базу данных")
    @PostMapping("/v1/sections")
    ResponseSection addSection (@RequestBody CreateRequestSection createRequest);

    @Operation(summary = "Изменение параметров участка")
    @PutMapping("/v1/sections/{id}")
    ResponseSection updateSection (@PathVariable UUID id, @RequestBody UpdateRequestSection updateRequest);

    @Operation(summary = "Удаление участка из базы данных по id")
    @DeleteMapping("/v1/sections/{id}")
    void deleteSection(@PathVariable UUID id);
}