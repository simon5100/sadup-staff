package sadupstaff.controller.sectionemployee;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sadupstaff.dto.sectionemployee.UpdateSectionEmployeeDTO;
import sadupstaff.mapper.sectionemployee.MapperCreateSectionEmployee;
import sadupstaff.mapper.sectionemployee.MapperFindSectionEmployee;
import sadupstaff.mapper.sectionemployee.MapperUpdateSectionEmployee;
import sadupstaff.model.sectionemployee.CreateRequestSectionEmployee;
import sadupstaff.model.sectionemployee.ResponseSectionEmployee;
import sadupstaff.model.sectionemployee.UpdateRequestSectionEmployee;
import sadupstaff.service.sectionemployee.SectionEmployeeService;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class SectionEmployeeRESTControllerImpl implements SectionEmployeeRESTController {

    private final SectionEmployeeService sectionEmployeeService;
    private final MapperFindSectionEmployee mapperFindSectionEmployee;
    private final MapperCreateSectionEmployee mapperCreateSectionEmployee;
    private final MapperUpdateSectionEmployee mapperUpdateSectionEmployee;

    public List<ResponseSectionEmployee> showSectionEmployees() {

        return sectionEmployeeService.getAllSectionEmployee().stream()
                .map(sectionEmployeeDTO -> mapperFindSectionEmployee.DTOtoResponse(sectionEmployeeDTO))
                .collect(Collectors.toList());
    }

    public ResponseSectionEmployee showSectionEmployee(@PathVariable UUID id) {

        return mapperFindSectionEmployee.DTOtoResponse(sectionEmployeeService.getSectionEmployee(id));
    }

    public ResponseSectionEmployee addSectionEmployee (@RequestBody CreateRequestSectionEmployee createRequest) {

        return showSectionEmployee(sectionEmployeeService.saveSectionEmployee(mapperCreateSectionEmployee.toDTO(createRequest)));
    }

    public ResponseSectionEmployee updateSectionEmployee(@PathVariable UUID id, @RequestBody UpdateRequestSectionEmployee updateRequest) {
        UpdateSectionEmployeeDTO updateSectionEmployeeDTO = mapperUpdateSectionEmployee.updateRequestToUpdateDTO(updateRequest);
        sectionEmployeeService.updateSectionEmployee(id, updateSectionEmployeeDTO);
        return showSectionEmployee(id);
    }

    public void deleteSection(@PathVariable UUID id) {
        sectionEmployeeService.deleteSectionEmployee(id);
    }
}