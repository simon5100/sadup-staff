package sadupstaff.service.sectionemployee;

import sadupstaff.entity.district.SectionEmployee;
import java.util.List;
import java.util.UUID;

public interface SectionEmployeeService {

    public List<SectionEmployee> getAllSectionEmployee();

    public SectionEmployee getSectionEmployee(UUID id);

    public void saveSectionEmployee(SectionEmployee sectionEmployee);

    public void updateSectionEmployee(UUID id, SectionEmployee sectionEmployeeNew);

    public void deleteSectionEmployee(UUID id);
}
