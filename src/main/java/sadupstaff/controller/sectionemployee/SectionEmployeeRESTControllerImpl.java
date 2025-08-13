package sadupstaff.controller.sectionemployee;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sadupstaff.dto.request.sectionemployee.CreateSectionEmployeeRequest;
import sadupstaff.dto.response.SectionEmployeeResponse;
import sadupstaff.dto.request.sectionemployee.UpdateSectionEmployeeRequest;
import sadupstaff.service.sectionemployee.SectionEmployeeService;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class SectionEmployeeRESTControllerImpl implements SectionEmployeeRESTController {

    private final SectionEmployeeService sectionEmployeeService;

    public List<SectionEmployeeResponse> showSectionEmployees() {
        return sectionEmployeeService.getAllSectionEmployee();
    }

    public SectionEmployeeResponse showSectionEmployee(@PathVariable UUID id) {
        return sectionEmployeeService.getSectionEmployee(id);
    }

    public SectionEmployeeResponse addSectionEmployee (@RequestBody CreateSectionEmployeeRequest createRequest) {

        return sectionEmployeeService.saveNewSectionEmployee(createRequest);
    }

    public SectionEmployeeResponse updateSectionEmployee(@PathVariable UUID id, @RequestBody UpdateSectionEmployeeRequest updateRequest) {

        return sectionEmployeeService.updateSectionEmployee(id, updateRequest);
    }

    public void deleteSection(@PathVariable UUID id) {
        sectionEmployeeService.deleteSectionEmployee(id);
    }
}