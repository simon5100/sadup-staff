package sadupstaff.model.employee;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseEmployee {

    final private String personelNumber;

    final private String firstName;

    final private String lastName;

    final private String patronymic;

    final private String position;

    final private String departmentName;
}