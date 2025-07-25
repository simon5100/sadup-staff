package sadupstaff.model.district;

import lombok.AllArgsConstructor;
import lombok.Data;
import sadupstaff.dto.section.SectionDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UpdateRequestDistrict {

    final private String name;

    final private String description;
}