package sadupstaff.model.district;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import sadupstaff.dto.section.SectionDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CreateRequestDistrict {

    @NotBlank
    final private String name;

    @NotBlank
    final private String description;
}