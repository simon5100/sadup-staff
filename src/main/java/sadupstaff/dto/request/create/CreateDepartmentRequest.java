package sadupstaff.dto.request.create;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Сущность DTO создания отдела")
public class CreateDepartmentRequest {

    @NotBlank
    @Schema(description = "Имя отдела", example = "Центральный район")
    private String name;

    @Schema(description = "Описание отдела")
    private String description;
}
