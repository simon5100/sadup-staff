package sadupstaff.service.sectionemployee;

import sadupstaff.dto.request.sectionemployee.CreateRequestSectionEmployee;
import sadupstaff.dto.request.sectionemployee.UpdateRequestSectionEmployee;
import sadupstaff.dto.response.ResponseSectionEmployee;
import sadupstaff.dto.sectionemployee.SectionEmployeeDTO;
import java.util.List;
import java.util.UUID;

public interface SectionEmployeeService {

    List<ResponseSectionEmployee> getAllSectionEmployee();

    ResponseSectionEmployee getSectionEmployee(UUID id);

    SectionEmployeeDTO getSectionEmployeeForUpdate(UUID id);

    ResponseSectionEmployee saveNewSectionEmployee(CreateRequestSectionEmployee createRequest);

    ResponseSectionEmployee updateSectionEmployee(UUID id, UpdateRequestSectionEmployee updateRequest);

    void deleteSectionEmployee(UUID id);
}
