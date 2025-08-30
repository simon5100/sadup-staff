package sadupstaff.dto.request.create;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import sadupstaff.enums.DistrictNameEnum;

@Data
@AllArgsConstructor
@Schema(name = "Сущность DTO создания района")
public class CreateDistrictRequest {

    @NotBlank
    @Schema(description = "Имя района", example = "Центральный")
    private DistrictNameEnum name;

    @NotBlank
    @Schema(description = "максимальное число участков в района района", example = "5")
    private Integer maxNumberSection;

    @NotBlank
    @Schema(description = "Описание района")
    private String description;
}