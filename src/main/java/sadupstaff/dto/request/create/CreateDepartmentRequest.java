package sadupstaff.dto.request.create;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sadupstaff.enums.DepartmentNameEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Сущность DTO создания отдела")
public class CreateDepartmentRequest {

    @NotBlank
    @Schema(description = "Имя отдела", example = "Отдел правового обеспечения")
    private DepartmentNameEnum name;

    @NotBlank
    @Schema(description = "Максимальное число сотрудников в отделе", example = "5")
    private Integer maxNumberEmployees;

    @Schema(description = "Описание отдела")
    private String description;
}
