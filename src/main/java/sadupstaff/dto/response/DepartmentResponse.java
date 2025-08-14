package sadupstaff.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Сущность DTO для отображения отдела")
public class DepartmentResponse {

    @Schema(description = "Имя отдела", example = "Центральный район")
    private String name;

    @Schema(description = "Описание отдела")
    private String description;

    @Schema(description = "Дата и время создания отдела")
    private LocalDateTime createdAt;

    @Schema(description = "Дата и время изменения отдела")
    private LocalDateTime updatedAt;

    @Schema(description = "Список сущностей сотрудников, которые работают в отделе")
    private List<EmployeeResponse> emps;
}
