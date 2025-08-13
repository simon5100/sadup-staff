package sadupstaff.controller.district;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import sadupstaff.dto.request.district.CreateDistrictRequest;
import sadupstaff.dto.request.district.UpdateDistrictRequest;
import sadupstaff.dto.response.DistrictResponse;
import java.util.List;
import java.util.UUID;

@Tag(name = "District API", description = "API взаимодействующий с District")
@RequestMapping("/api")
public interface DistrictRESTController {

    @Operation(summary = "Вызов всего списка районов")
    @GetMapping("/v1/districts")
    List<DistrictResponse> showDistricts();

    @Operation(summary = "Вызов района по id")
    @GetMapping("/v1/districts/{id}")
    DistrictResponse showDistrict(@PathVariable UUID id);

    @Operation(summary = "Создание нового района")
    @PostMapping("/v1/districts")
    DistrictResponse addDistrict (@RequestBody CreateDistrictRequest createRequest);

    @Operation(summary = "Изменение параметров района")
    @PutMapping("/v1/districts/{id}")
    DistrictResponse updateDistrict (@PathVariable UUID id, @RequestBody UpdateDistrictRequest updateReques);

    @Operation(summary = "Удаление района по id")
    @DeleteMapping("/v1/districts/{id}")
    void deleteDistrict(@PathVariable UUID id);
}