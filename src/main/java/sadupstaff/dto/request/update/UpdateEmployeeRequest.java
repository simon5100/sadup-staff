package sadupstaff.dto.request.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sadupstaff.enums.PositionEmployeeEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Сущность DTO для запроса на обновление работника управления")
public class UpdateEmployeeRequest {

    @Schema(description = "Персональный номер сотрудника", example = "ПН-1", required = true)
    private String personelNumber;

    @Schema(description = "Имя сотрудника", example = "Иван", required = true)
    private String firstName;

    @Schema(description = "Фамилия сотрудника", example = "Иванов", required = true)
    private String lastName;

    @Schema(description = "Отчество сотрудника (при наличии)", example = "Иванович")
    private String patronymic;

    @Schema(description = "Должность сотрудника", example = "Консультанат", required = true)
    private PositionEmployeeEnum position;
}