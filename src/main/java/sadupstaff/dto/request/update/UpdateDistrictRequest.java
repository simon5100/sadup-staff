package sadupstaff.dto.request.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sadupstaff.enums.DistrictName;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Сущность DTO для запроса на обнговление района")
public class UpdateDistrictRequest {

    @Schema(description = "Имя района", example = "Центральный район")
    private DistrictName name;

    @Schema(description = "Описание района")
    private String description;
}