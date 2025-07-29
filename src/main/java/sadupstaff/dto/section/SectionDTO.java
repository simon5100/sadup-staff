package sadupstaff.dto.section;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import sadupstaff.dto.sectionemployee.SectionEmployeeDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Schema(name = "Сущность участка")
public class SectionDTO {

    @Schema(description = "Идентификатор участка", example = "175fa2d0-b608-4ee1-b50e-02f5d51ee9cc")
    final private UUID id;

    @Schema(description = "Персональный номер участка", example = "54MS0000")
    final private String personelNumber;

    @Schema(description = "Имя участка", example = "1-участок центрального района")
    final private String name;

    @Schema(description = "Дата и время создания участка")
    final private LocalDateTime createdAt;

    @Schema(description = "Дата и время изменения участка")
    final private LocalDateTime updatedAt;

    @Schema(description = "Имя района в котором находится участок", example = "Центральный район")
    final private String districtName;

    @Schema(description = "Список сущностей сотрудников которые работают на участке")
    final private List<SectionEmployeeDTO> empsSect;
}