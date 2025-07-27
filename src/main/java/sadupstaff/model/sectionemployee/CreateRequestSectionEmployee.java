package sadupstaff.model.sectionemployee;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sadupstaff.dto.section.SectionDTO;
import sadupstaff.enums.SectionEmployeeEnum;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRequestSectionEmployee {

    @NotBlank
    private String personelNumber;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private String patronymic;

    @NotBlank
    private String position;

    @NotBlank
    private String sectionName;
}