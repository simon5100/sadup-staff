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
@Schema(name = "Сущность DTO отображения района")
public class DistrictResponse {

    @Schema(description = "Имя района", example = "Центральный район")
    private String name;

    @Schema(description = "Описание района")
    private String description;

    @Schema(description = "Дата и время создания района")
    private LocalDateTime createdAt;

    @Schema(description = "Дата и время изменения района")
    private LocalDateTime updatedAt;

    @Schema(description = "Список сущностей участков, которые находятся в районе")
    private List<SectionResponse> sections;
}