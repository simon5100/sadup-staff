package sadupstaff.dto.sectionemployee;

import lombok.*;
import sadupstaff.enums.SectionEmployeeEnum;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class SectionEmployeeDTO {

    final private UUID id;

    final private String personelNumber;

    final private String firstName;

    final private String lastName;

    final private String patronymic;

    final private SectionEmployeeEnum position;

    final private String sectionName;

    final private LocalDateTime createdAt;

    final private LocalDateTime updatedAt;
}