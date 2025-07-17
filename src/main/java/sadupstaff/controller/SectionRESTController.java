package sadupstaff.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sadupstaff.entity.district.Section;
import sadupstaff.service.section_service.SectionService;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SectionRESTController {

    private final SectionService sectionService;

    @GetMapping("/sections")
    public List<Section> showSections() {
        return sectionService.getAllSection();
    }

    @GetMapping("/sections/{id}")
    public Section showSection(@PathVariable UUID id) {
        return sectionService.getSection(id);
    }

    @PostMapping("/sections")
    public Section addSection (@RequestBody Section section) {
        sectionService.saveSection(section);
        return section;
    }

    @PutMapping("/sections/{id}")
    public Section updateSection (@PathVariable UUID id, @RequestBody Section section) {
        sectionService.updateSection(id, section);
        return sectionService.getSection(id);
    }
    @DeleteMapping("/sections/{id}")
    public void deleteSection(@PathVariable UUID id) {
        sectionService.deleteSection(id);
    }
}