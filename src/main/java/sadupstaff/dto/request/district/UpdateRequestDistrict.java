package sadupstaff.dto.request.district;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateRequestDistrict {

    final private String name;

    final private String description;
}