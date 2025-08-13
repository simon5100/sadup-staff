package sadupstaff.dto.request.employee;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Сущность DTO создания работника управления")
public class CreateEmployeeRequest {

    @NotBlank
    @Schema(description = "Персональный номер сотрудника", example = "ПН-1")
    private String personelNumber;

    @NotBlank
    @Schema(description = "Имя сотрудника", example = "Иван")
    private String firstName;

    @NotBlank
    @Schema(description = "Фамилия сотрудника", example = "Иванов")
    private String lastName;

    @Schema(description = "Отчество сотрудника (при наличии)", example = "Иванович")
    private String patronymic;

    @NotBlank
    @Schema(description = "Должность сотрудника", example = "Консультанат")
    private String position;

    @NotBlank
    @Schema(description = "Отдел в котором работает сотрудник", example = "Отдел правового обеспечения ")
    private String departmentName;
}