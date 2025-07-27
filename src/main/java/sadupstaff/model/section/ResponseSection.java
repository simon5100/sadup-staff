package sadupstaff.model.section;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sadupstaff.model.sectionemployee.ResponseSectionEmployee;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseSection {

    private UUID id;

    private String personelNumber;

    private String name;

    private String districtName;

    private List<ResponseSectionEmployee> empsSect;
}