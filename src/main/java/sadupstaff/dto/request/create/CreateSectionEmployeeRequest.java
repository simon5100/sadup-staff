package sadupstaff.dto.request.create;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Сущность DTO создания работника участка")
public class CreateSectionEmployeeRequest {

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
    private String position;

    @Schema(description = "Участок на котором работает сотрудник", example = "1-участок центрального района", required = true)
    private String sectionName;
}