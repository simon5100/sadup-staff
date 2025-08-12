package sadupstaff.controller.section;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sadupstaff.dto.request.section.CreateRequestSection;
import sadupstaff.dto.response.ResponseSection;
import sadupstaff.dto.request.section.UpdateRequestSection;
import sadupstaff.service.section.SectionService;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class SectionRESTControllerImpl implements SectionRESTController {

    private final SectionService sectionService;

    public List<ResponseSection> showSections() {
        return sectionService.getAllSection();
    }

    public ResponseSection showSection(@PathVariable UUID id) {
        return sectionService.getSectionById(id);
    }

    public ResponseSection addSection (@RequestBody CreateRequestSection createRequest) {
        return sectionService.saveNewSection(createRequest);
    }

    public ResponseSection updateSection (@PathVariable UUID id, @RequestBody UpdateRequestSection updateRequest) {
        return sectionService.updateSection(id, updateRequest);
    }

    public void deleteSection(@PathVariable UUID id) {
        sectionService.deleteSection(id);
    }
}