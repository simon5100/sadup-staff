package sadupstaff.dto.sectionemployee;

import lombok.Data;
import sadupstaff.dto.section.SectionDTO;
import sadupstaff.enums.SectionEmployeeEnum;

import java.time.LocalDateTime;

@Data
public class UpdateSectionEmployeeDTO {

    private String personelNumber;

    private String firstName;

    private String lastName;

    private String patronymic;

    private SectionEmployeeEnum position;

    private SectionDTO empSection;
}