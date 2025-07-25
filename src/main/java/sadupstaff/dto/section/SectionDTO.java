package sadupstaff.dto.section;

import lombok.*;
import sadupstaff.dto.district.DistrictDTO;
import sadupstaff.dto.sectionemployee.SectionEmployeeDTO;

import java.util.ArrayList;
import java.util.List;

@Data
public class SectionDTO {

    private String personelNumber;

    private String name;

    private DistrictDTO sectDistrict;

    private List<SectionEmployeeDTO> empsSect = new ArrayList<>();
}