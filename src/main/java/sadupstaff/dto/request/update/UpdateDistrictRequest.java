package sadupstaff.dto.request.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sadupstaff.enums.DistrictNameEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Сущность DTO для запроса на обнговление района")
public class UpdateDistrictRequest {

    @Schema(description = "Имя района", example = "Центральный район")
    private DistrictNameEnum name;

    @Schema(description = "Описание района")
    private String description;
}