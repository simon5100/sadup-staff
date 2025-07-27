package sadupstaff.dto.section;

import lombok.*;
import sadupstaff.dto.sectionemployee.SectionEmployeeDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class SectionDTO {

    final private UUID id;

    final private String personelNumber;

    final private String name;

    final private LocalDateTime createdAt;

    final private LocalDateTime updatedAt;

    final private String districtName;

    final private List<SectionEmployeeDTO> empsSect;
}