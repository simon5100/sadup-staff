package sadupstaff.dto.request.create;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(name = "Сущность DTO создания района")
public class CreateDistrictRequest {

    @NotBlank
    @Schema(description = "Имя района", example = "Центральный район")
    final private String name;

    @NotBlank
    @Schema(description = "Описание района")
    final private String description;
}