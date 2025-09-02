package sadupstaff.dto.request.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sadupstaff.enums.DepartmentName;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Сущность DTO для запроса на обновления отдела")
public class UpdateDepartmentRequest {

    @Schema(description = "Имя отдела", example = "Отдел правового обеспечения")
    private DepartmentName name;

    @Schema(description = "Описание отдела")
    private String description;
}
