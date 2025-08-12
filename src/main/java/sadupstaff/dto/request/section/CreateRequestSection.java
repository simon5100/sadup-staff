package sadupstaff.dto.request.section;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRequestSection {

    @NotBlank
    private String personelNumber;

    @NotBlank
    private String name;

    @NotBlank
    private String districtName;
}