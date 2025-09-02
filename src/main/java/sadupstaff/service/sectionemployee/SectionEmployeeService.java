package sadupstaff.service.sectionemployee;

import sadupstaff.dto.request.create.CreateSectionEmployeeRequest;
import sadupstaff.dto.request.update.UpdateSectionEmployeeRequest;
import sadupstaff.dto.response.SectionEmployeeResponse;
import sadupstaff.entity.district.SectionEmployee;
import java.util.List;
import java.util.UUID;

public interface SectionEmployeeService {

    List<SectionEmployeeResponse> getAllSectionEmployee();

    SectionEmployeeResponse getSectionEmployee(UUID id);

    SectionEmployeeResponse saveNewSectionEmployee(CreateSectionEmployeeRequest createRequest);

    SectionEmployeeResponse updateSectionEmployee(UUID id, UpdateSectionEmployeeRequest updateRequest);

    void deleteSectionEmployee(UUID id);
}
