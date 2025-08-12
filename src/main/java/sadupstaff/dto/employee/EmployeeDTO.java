package sadupstaff.dto.employee;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@Schema(name = "Сущность работника управления")
public class EmployeeDTO {

    @Schema(description = "Идентификатор сотрудника", example = "175fa2d0-b608-4ee1-b50e-02f5d51ee9cc", required = true)
    final private UUID id;

    @Schema(description = "Персональный номер сотрудника", example = "ПН-1", required = true)
    final private String personelNumber;

    @Schema(description = "Имя сотрудника", example = "Иван", required = true)
    final private String firstName;

    @Schema(description = "Фамилия сотрудника", example = "Иванов", required = true)
    final private String lastName;

    @Schema(description = "Отчество сотрудника (при наличии)", example = "Иванович")
    final private String patronymic;

    @Schema(description = "Должность сотрудника", example = "Консультанат", required = true)
    final private String position;

    @Schema(description = "Отдел в котором работает сотрудник", example = "Отдел правового обеспечения ", required = true)
    final private String departmentName;

    @Schema(description = "Дата и время создания сотрудника")
    private LocalDateTime createdAt;

    @Schema(description = "Дата и время изменения сотрудника")
    private LocalDateTime updatedAt;
}