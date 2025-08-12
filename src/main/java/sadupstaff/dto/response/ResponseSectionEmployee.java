package sadupstaff.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseSectionEmployee {

    private String personelNumber;

    private String firstName;

    private String lastName;

    private String patronymic;

    private String position;

    private String sectionName;
}
