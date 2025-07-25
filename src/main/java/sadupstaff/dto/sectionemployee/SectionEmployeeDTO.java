package sadupstaff.dto.sectionemployee;

import lombok.*;
import sadupstaff.dto.section.SectionDTO;
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