package sadupstaff.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Сущность DTO отображения участка")
public class SectionResponse {

    @Schema(description = "Персональный номер участка", example = "54MS0000")
    private String personelNumber;

    @Schema(description = "Имя участка", example = "1-участок центрального района")
    private String name;

    @Schema(description = "Имя района в котором находится участок", example = "Центральный район")
    private String districtName;

    @Schema(description = "Список сущностей сотрудников которые работают на участке")
    private List<SectionEmployeeResponse> empsSect;
}