package sadupstaff.dto.district;

import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Data
public class DistrictDTO {

    private String name;

    private String description;

    private List<SectionDTO> sections = new ArrayList<>();
}