package sadupstaff.dto.district;

import lombok.*;
import sadupstaff.dto.section.SectionDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class DistrictDTO {

    final private UUID id;

    final private String name;

    final private String description;

    final private LocalDateTime createdAt;

    final private LocalDateTime updatedAt;

    final private List<SectionDTO> sections;
}