package sadupstaff.dto.section;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import sadupstaff.entity.district.District;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Schema(name = "Сущность DTO для обновление участка")
public class UpdateSectionDTO {

    @Schema(description = "Идентификатор участка", example = "175fa2d0-b608-4ee1-b50e-02f5d51ee9cc")
    private UUID id;

    @Schema(description = "Персональный номер участка", example = "54MS0000")
    private String personelNumber;

    @Schema(description = "Имя участка", example = "1-участок центрального района")
    private String name;

    @Schema(description = "Район в котором находится участок")
    private District district;

    @Schema(description = "Дата и время создания участка")
    private LocalDateTime createdAt;

    @Schema(description = "Дата и время изменения участка")
    private LocalDateTime updatedAt;
}