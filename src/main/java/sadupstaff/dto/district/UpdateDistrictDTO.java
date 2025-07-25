package sadupstaff.dto.district;

import lombok.AllArgsConstructor;
import lombok.Data;
import sadupstaff.dto.section.SectionDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UpdateDistrictDTO {

    private UUID id;

    private String name;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<SectionDTO> sections;
}