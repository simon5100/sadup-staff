package sadupstaff.dto.request.section;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Сущность DTO создания участка")
public class CreateSectionRequest {

    @NotBlank
    @Schema(description = "Персональный номер участка", example = "54MS0000")
    private String personelNumber;

    @NotBlank
    @Schema(description = "Имя участка", example = "1-участок центрального района")
    private String name;

    @NotBlank
    @Schema(description = "Имя района в котором находится участок", example = "Центральный район")
    private String districtName;
}