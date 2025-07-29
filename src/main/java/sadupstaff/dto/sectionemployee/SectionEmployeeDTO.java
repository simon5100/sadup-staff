package sadupstaff.dto.sectionemployee;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import sadupstaff.enums.SectionEmployeeEnum;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@Schema(name = "Сущность работника участка")
public class SectionEmployeeDTO {

    @Schema(description = "Идентификатор сотрудника", example = "175fa2d0-b608-4ee1-b50e-02f5d51ee9cc", required = true)
    final private UUID id;

    @Schema(description = "Персональный номер сотрудника", example = "ЦЕН-1", required = true)
    final private String personelNumber;

    @Schema(description = "Имя сотрудника", example = "Иван", required = true)
    final private String firstName;

    @Schema(description = "Фамилия сотрудника", example = "Иванов", required = true)
    final private String lastName;

    @Schema(description = "Отчество сотрудника (при наличии)", example = "Иванович")
    final private String patronymic;

    @Schema(description = "Должность сотрудника",
            allowableValues = { "Судья",
                                "Помощник мирового судьи",
                                "Секретарь судебного заседания",
                                "Секретарь судебного участка",
                                "Специалист"},
            required = true)
    final private SectionEmployeeEnum position;

    @Schema(description = "Участок на котором работает сотрудник", example = "1-участок центрального района", required = true)
    final private String sectionName;

    @Schema(description = "Дата и время создания сотрудника")
    final private LocalDateTime createdAt;

    @Schema(description = "Дата и время изменения сотрудника")
    final private LocalDateTime updatedAt;
}