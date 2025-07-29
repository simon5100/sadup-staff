package sadupstaff.controller.section;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sadupstaff.dto.section.UpdateSectionDTO;
import sadupstaff.mapper.section.MapperCreateSection;
import sadupstaff.mapper.section.MapperFindSection;
import sadupstaff.mapper.section.MapperUpdateSection;
import sadupstaff.model.section.CreateRequestSection;
import sadupstaff.model.section.ResponseSection;
import sadupstaff.model.section.UpdateRequestSection;
import sadupstaff.service.section.SectionService;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class SectionRESTControllerImpl implements SectionRESTController {

    private final SectionService sectionService;
    private final MapperFindSection mapperFindSection;
    private final MapperCreateSection mapperCreateSection;
    private final MapperUpdateSection mapperUpdateSection;

    public List<ResponseSection> showSections() {

        return sectionService.getAllSection().stream()
                .map(sectionDTO -> mapperFindSection.DTOToResponseSection(sectionDTO))
                .collect(Collectors.toList());
    }

    public ResponseSection showSection(@PathVariable UUID id) {

        return mapperFindSection.DTOToResponseSection(sectionService.getSectionById(id));
    }

    public ResponseSection addSection (@RequestBody CreateRequestSection createRequest) {
        return showSection(sectionService.saveSection(mapperCreateSection.createSectionToDTO(createRequest)));
    }

    public ResponseSection updateSection (@PathVariable UUID id, @RequestBody UpdateRequestSection updateRequest) {
        UpdateSectionDTO updateSectionDTO = mapperUpdateSection.updateRequestToDTO(updateRequest);
        sectionService.updateSection(id, updateSectionDTO);
        return showSection(id);
    }

    public void deleteSection(@PathVariable UUID id) {
        sectionService.deleteSection(id);
    }
}