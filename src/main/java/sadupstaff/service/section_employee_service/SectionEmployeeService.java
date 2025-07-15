package sadupstaff.service.section_employee_service;

import sadupstaff.entity.district.SectionEmployee;

import java.util.List;
import java.util.UUID;

public interface SectionEmployeeService {
    public List<SectionEmployee> getAllSectionEmployee();

    public SectionEmployee getSectionEmployee(UUID id);

    public void saveSectionEmployee(SectionEmployee sectionEmployee);

    public void deleteSectionEmployee(UUID id);
}
