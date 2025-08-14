package sadupstaff.controller.section;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import sadupstaff.dto.request.create.CreateSectionRequest;
import sadupstaff.dto.response.SectionResponse;
import sadupstaff.dto.request.update.UpdateSectionRequest;

import java.util.List;
import java.util.UUID;

@Tag(name = "Section API", description = "API взаимодействующий с Section")
@RequestMapping("/api")
public interface SectionRESTController {

    @Operation(summary = "Вызов всего списка участков")
    @GetMapping("/v1/sections")
    List<SectionResponse> showSections();

    @Operation(summary = "Вызов участка по id")
    @GetMapping("/v1/sections/{id}")
    SectionResponse showSection(@PathVariable UUID id);

    @Operation(summary = "Создание нового участка")
    @PostMapping("/v1/sections")
    SectionResponse addSection (@RequestBody CreateSectionRequest createRequest);

    @Operation(summary = "Изменение параметров участка")
    @PutMapping("/v1/sections/{id}")
    SectionResponse updateSection (@PathVariable UUID id, @RequestBody UpdateSectionRequest updateRequest);

    @Operation(summary = "Удаление участка id")
    @DeleteMapping("/v1/sections/{id}")
    void deleteSection(@PathVariable UUID id);
}