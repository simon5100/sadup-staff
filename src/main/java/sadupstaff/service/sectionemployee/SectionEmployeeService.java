package sadupstaff.service.sectionemployee;

import sadupstaff.dto.sectionemployee.SectionEmployeeDTO;
import sadupstaff.dto.sectionemployee.UpdateSectionEmployeeDTO;
import java.util.List;
import java.util.UUID;

public interface SectionEmployeeService {

    public List<SectionEmployeeDTO> getAllSectionEmployee();

    public SectionEmployeeDTO getSectionEmployee(UUID id);

    public UUID saveSectionEmployee(SectionEmployeeDTO sectionEmployeeDTO);

    public void updateSectionEmployee(UUID id, UpdateSectionEmployeeDTO updateSectionEmployeeDTO);

    public void deleteSectionEmployee(UUID id);
}
