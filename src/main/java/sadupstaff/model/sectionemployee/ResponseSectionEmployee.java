package sadupstaff.model.sectionemployee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseSectionEmployee {

    private UUID id;

    private String personelNumber;

    private String firstName;

    private String lastName;

    private String patronymic;

    private String position;

    private String sectionName;
}
