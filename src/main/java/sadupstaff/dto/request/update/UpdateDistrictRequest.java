package sadupstaff.dto.request.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(name = "Сущность DTO для запроса на обнговление района")
public class UpdateDistrictRequest {

    @Schema(description = "Имя района", example = "Центральный район")
    final private String name;

    @Schema(description = "Описание района")
    final private String description;
}