package sadupstaff.controller.district;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import sadupstaff.dto.request.district.CreateRequestDistrict;
import sadupstaff.dto.response.ResponseDistrict;
import sadupstaff.dto.request.district.UpdateRequestDistrict;
import java.util.List;
import java.util.UUID;

@Tag(name = "District API", description = "Класс взаимодействующий с District")
@RequestMapping("/api")
public interface DistrictRESTController {

    @Operation(summary = "Вызов всего списка районов")
    @GetMapping("/v1/districts")
    List<ResponseDistrict> showDistricts();

    @Operation(summary = "Вызов района по id")
    @GetMapping("/v1/districts/{id}")
    ResponseDistrict showDistrict(@PathVariable UUID id);

    @Operation(summary = "Создание нового района и добавление его в базу данных")
    @PostMapping("/v1/districts")
    ResponseDistrict addDistrict (@RequestBody CreateRequestDistrict createRequest);

    @Operation(summary = "Изменение параметров района")
    @PutMapping("/v1/districts/{id}")
    ResponseDistrict updateDistrict (@PathVariable UUID id, @RequestBody UpdateRequestDistrict updateReques);

    @Operation(summary = "Удаление района из базы данных по id")
    @DeleteMapping("/v1/districts/{id}")
    void deleteDistrict(@PathVariable UUID id);
}