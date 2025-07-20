package sadupstaff.dto.district;

import lombok.*;
import sadupstaff.enums.SectionEmployeeEnum;

@Data
public class SectionEmployeeDTO {

    private String personelNumber;

    private String firstName;

    private String lastName;

    private String patronymic;

    private SectionEmployeeEnum position;

    private SectionDTO empSection;
}