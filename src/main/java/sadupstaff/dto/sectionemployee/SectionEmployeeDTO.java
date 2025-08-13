package sadupstaff.dto.sectionemployee;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import sadupstaff.enums.SectionEmployeeEnum;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@Schema(name = "Сущность DTO работника участка")
public class SectionEmployeeDTO {

    @Schema(description = "Идентификатор сотрудника", example = "175fa2d0-b608-4ee1-b50e-02f5d51ee9cc", required = true)
    private UUID id;

    @Schema(description = "Персональный номер сотрудника", example = "ЦЕН-1", required = true)
    private String personelNumber;

    @Schema(description = "Имя сотрудника", example = "Иван", required = true)
    private String firstName;

    @Schema(description = "Фамилия сотрудника", example = "Иванов", required = true)
    private String lastName;

    @Schema(description = "Отчество сотрудника (при наличии)", example = "Иванович")
    private String patronymic;

    @Schema(description = "Должность сотрудника",
            allowableValues = { "Судья",
                                "Помощник мирового судьи",
                                "Секретарь судебного заседания",
                                "Секретарь судебного участка",
                                "Специалист"},
            required = true)
    private SectionEmployeeEnum position;

    @Schema(description = "Участок на котором работает сотрудник", example = "1-участок центрального района", required = true)
    private String sectionName;

    @Schema(description = "Дата и время создания сотрудника")
    private LocalDateTime createdAt;

    @Schema(description = "Дата и время изменения сотрудника")
    private LocalDateTime updatedAt;
}