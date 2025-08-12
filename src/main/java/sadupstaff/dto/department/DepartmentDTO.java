package sadupstaff.dto.department;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import sadupstaff.dto.employee.EmployeeDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@Schema(name = "Сущность отдела")
public class DepartmentDTO {

    @Schema(description = "Идентификатор района", example = "175fa2d0-b608-4ee1-b50e-02f5d51ee9cc", required = true)
    final private UUID id;

    @Schema(description = "Имя отдела", example = "Центральный район")
    final private String name;

    @Schema(description = "Описание отдела")
    final private String description;

    @Schema(description = "Дата и время создания отдела")
    final private LocalDateTime createdAt;

    @Schema(description = "Дата и время изменения отдела")
    final private LocalDateTime updatedAt;

    @Schema(description = "Список сущностей сотрудников, которые работают в отделе")
    final private List<EmployeeDTO> emps;
}
