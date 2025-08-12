package sadupstaff.controller.sectionemployee;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sadupstaff.dto.request.sectionemployee.CreateRequestSectionEmployee;
import sadupstaff.dto.response.ResponseSectionEmployee;
import sadupstaff.dto.request.sectionemployee.UpdateRequestSectionEmployee;
import sadupstaff.service.sectionemployee.SectionEmployeeService;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class SectionEmployeeRESTControllerImpl implements SectionEmployeeRESTController {

    private final SectionEmployeeService sectionEmployeeService;

    public List<ResponseSectionEmployee> showSectionEmployees() {
        return sectionEmployeeService.getAllSectionEmployee();
    }

    public ResponseSectionEmployee showSectionEmployee(@PathVariable UUID id) {
        return sectionEmployeeService.getSectionEmployee(id);
    }

    public ResponseSectionEmployee addSectionEmployee (@RequestBody CreateRequestSectionEmployee createRequest) {

        return sectionEmployeeService.saveNewSectionEmployee(createRequest);
    }

    public ResponseSectionEmployee updateSectionEmployee(@PathVariable UUID id, @RequestBody UpdateRequestSectionEmployee updateRequest) {

        return sectionEmployeeService.updateSectionEmployee(id, updateRequest);
    }

    public void deleteSection(@PathVariable UUID id) {
        sectionEmployeeService.deleteSectionEmployee(id);
    }
}