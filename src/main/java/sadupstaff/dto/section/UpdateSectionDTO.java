package sadupstaff.dto.section;

import lombok.Data;
import sadupstaff.dto.district.DistrictDTO;
import sadupstaff.dto.sectionemployee.SectionEmployeeDTO;
import java.util.List;
import java.util.UUID;

@Data
public class UpdateSectionDTO {

    final private UUID id;

    private String personelNumber;

    private String name;

    private DistrictDTO sectDistrict;

    private List<SectionEmployeeDTO> empsSect;
}