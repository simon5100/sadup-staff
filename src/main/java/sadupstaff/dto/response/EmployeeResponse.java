package sadupstaff.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Сущность DTO отображения работника управления")
public class EmployeeResponse {

    @Schema(description = "Персональный номер сотрудника", example = "ПН-1", required = true)
    private String personelNumber;

    @Schema(description = "Имя сотрудника", example = "Иван", required = true)
    private String firstName;

    @Schema(description = "Фамилия сотрудника", example = "Иванов", required = true)
    private String lastName;

    @Schema(description = "Отчество сотрудника (при наличии)", example = "Иванович")
    private String patronymic;

    @Schema(description = "Должность сотрудника", example = "Консультанат", required = true)
    private String position;

    @Schema(description = "Отдел в котором работает сотрудник", example = "Отдел правового обеспечения ", required = true)
    private String departmentName;

    @Schema(description = "Дата и время создания сотрудника")
    private LocalDateTime createdAt;

    @Schema(description = "Дата и время изменения сотрудника")
    private LocalDateTime updatedAt;
}