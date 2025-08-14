package sadupstaff.controller.section;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sadupstaff.dto.request.create.CreateSectionRequest;
import sadupstaff.dto.request.update.UpdateSectionRequest;
import sadupstaff.dto.response.SectionResponse;
import sadupstaff.service.section.SectionService;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class SectionRESTControllerImpl implements SectionRESTController {

    private final SectionService sectionService;

    public List<SectionResponse> showSections() {
        return sectionService.getAllSection();
    }

    public SectionResponse showSection(@PathVariable UUID id) {
        return sectionService.getSectionById(id);
    }

    public SectionResponse addSection (@RequestBody CreateSectionRequest createRequest) {
        return sectionService.saveSection(createRequest);
    }

    public SectionResponse updateSection (@PathVariable UUID id, @RequestBody UpdateSectionRequest updateRequest) {
        return sectionService.updateSection(id, updateRequest);
    }

    public void deleteSection(@PathVariable UUID id) {
        sectionService.deleteSection(id);
    }
}