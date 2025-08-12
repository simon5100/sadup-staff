package sadupstaff.dto.request.section;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRequestSection {

    private String personelNumber;

    private String name;

    private String districtName;
}