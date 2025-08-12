package sadupstaff.dto.request.district;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateRequestDistrict {

    @NotBlank
    final private String name;

    @NotBlank
    final private String description;
}