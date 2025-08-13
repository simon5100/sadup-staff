package sadupstaff.dto.department;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@Schema(name = "Сущность DTO обновления отдела")
public class UpdateDepartmentDTO {

    private UUID id;

    @Schema(description = "Имя отдела", example = "Центральный район")
    private String name;

    @Schema(description = "Описание отдела")
    private String description;

    @Schema(description = "Дата и время создания отдела")
    private LocalDateTime createdAt;

    @Schema(description = "Дата и время изменения отдела")
    private LocalDateTime updatedAt;
}
