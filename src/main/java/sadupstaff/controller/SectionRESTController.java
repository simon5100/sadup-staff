package sadupstaff.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sadupstaff.entity.district.Section;
import sadupstaff.service.section_service.SectionService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
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

    @PutMapping("/sections")
    public Section updateSection (@RequestBody Section section) {
        sectionService.saveSection(section);

        return section;
    }
    @DeleteMapping("/sections/{id}")
    public String deleteSection(@PathVariable UUID id) {
        Section section = sectionService.getSection(id);

        sectionService.deleteSection(id);

        return section.getClass().getName() + " удален";
    }
}
