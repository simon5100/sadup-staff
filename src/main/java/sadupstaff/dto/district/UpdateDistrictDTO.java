package sadupstaff.dto.district;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@Schema(name = "Сущность DTO обновления района")
public class UpdateDistrictDTO {

    @Schema(description = "Идентификатор района", example = "175fa2d0-b608-4ee1-b50e-02f5d51ee9cc", required = true)
    private UUID id;

    @Schema(description = "Имя района", example = "Центральный район")
    private String name;

    @Schema(description = "Описание района")
    private String description;

    @Schema(description = "Дата и время создания района")
    private LocalDateTime createdAt;

    @Schema(description = "Дата и время изменения района")
    private LocalDateTime updatedAt;
}