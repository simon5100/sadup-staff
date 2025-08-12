package sadupstaff.dto.request.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRequestEmployee {

    private String personelNumber;

    private String firstName;

    private String lastName;

    private String patronymic;

    private String position;

    private String departmentName;
}