package sadupstaff.dto.district;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import sadupstaff.dto.section.SectionDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@Schema(name = "Сущность района")
public class DistrictDTO {

    @Schema(description = "Идентификатор района", example = "175fa2d0-b608-4ee1-b50e-02f5d51ee9cc", required = true)
    final private UUID id;

    @Schema(description = "Имя района", example = "Центральный район")
    final private String name;

    @Schema(description = "Описание района")
    final private String description;

    @Schema(description = "Дата и время создания района")
    final private LocalDateTime createdAt;

    @Schema(description = "Дата и время изменения района")
    final private LocalDateTime updatedAt;

    @Schema(description = "Список сущностей участков, которые находятся в районе")
    final private List<SectionDTO> sections;
}