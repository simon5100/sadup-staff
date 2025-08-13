package sadupstaff.dto.employee;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import sadupstaff.entity.management.Department;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@Schema(name = "Сущность DTO для обновления работника управления")
public class UpdateEmployeeDTO {

    @Schema(description = "Идентификатор сотрудника", example = "175fa2d0-b608-4ee1-b50e-02f5d51ee9cc")
    private UUID id;

    @Schema(description = "Персональный номер сотрудника", example = "ПН-1")
    private String personelNumber;

    @Schema(description = "Имя сотрудника", example = "Иван")
    private String firstName;

    @Schema(description = "Фамилия сотрудника", example = "Иванов")
    private String lastName;

    @Schema(description = "Отчество сотрудника (при наличии)", example = "Иванович")
    private String patronymic;

    @Schema(description = "Должность сотрудника", example = "Консультанат")
    private String position;

    @Schema(description = "Отдел в котором работает сотрудник")
    private Department department;

    @Schema(description = "Дата и время создания сотрудника")
    private LocalDateTime createdAt;

    @Schema(description = "Дата и время изменения сотрудника")
    private LocalDateTime updatedAt;
}