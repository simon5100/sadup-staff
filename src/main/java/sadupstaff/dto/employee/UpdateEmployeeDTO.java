package sadupstaff.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UpdateEmployeeDTO {

    private UUID id;

    private String personelNumber;

    private String firstName;

    private String lastName;

    private String patronymic;

    private String position;

    private String departmentName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}