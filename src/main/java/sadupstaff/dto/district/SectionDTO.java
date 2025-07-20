package sadupstaff.dto.district;

import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Data
public class SectionDTO {

    private String personelNumber;

    private String name;

    private DistrictDTO sectDistrict;

    private List<SectionEmployeeDTO> empsSect = new ArrayList<>();
}