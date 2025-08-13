package sadupstaff.dto.request.section;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Сущность DTO для запроса на обновление участка")
public class UpdateSectionRequest {

    @Schema(description = "Персональный номер участка", example = "54MS0000")
    private String personelNumber;

    @Schema(description = "Имя участка", example = "1-участок центрального района")
    private String name;
}