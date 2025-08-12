package sadupstaff.dto.request.sectionemployee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRequestSectionEmployee {

    private String personelNumber;

    private String firstName;

    private String lastName;

    private String patronymic;

    private String position;
}