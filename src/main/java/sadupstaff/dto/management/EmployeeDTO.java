package sadupstaff.dto.management;

import lombok.*;
import java.util.UUID;

@Data
@AllArgsConstructor
public class EmployeeDTO {

    final private UUID id;

    final private String personelNumber;

    final private String firstName;

    final private String lastName;

    final private String patronymic;

    final private String position;

    final private String departmentName;
}