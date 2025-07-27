package sadupstaff.model.section;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRequestSection {

    private UUID id;

    private String personelNumber;

    private String name;

    private String districtName;
}