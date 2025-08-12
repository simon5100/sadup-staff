package sadupstaff.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseSection {

    private String personelNumber;

    private String name;

    private String districtName;

    private List<ResponseSectionEmployee> empsSect;
}