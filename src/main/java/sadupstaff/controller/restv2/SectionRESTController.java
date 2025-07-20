package sadupstaff.controller.restv2;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sadupstaff.entity.district.Section;
import sadupstaff.service.section.SectionService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SectionRESTController {

    private final SectionService sectionService;

    @GetMapping("/v2/sections")
    public List<Section> showSections() {
        return sectionService.getAllSection();
    }

    @GetMapping("/v2/sections/{id}")
    public Section showSection(@PathVariable UUID id) {
        return sectionService.getSection(id);
    }

    @PostMapping("/v2/sections")
    public Section addSection (@RequestBody Section section) {
        sectionService.saveSection(section);
        return section;
    }

    @PutMapping("/v2/sections/{id}")
    public Section updateSection (@PathVariable UUID id, @RequestBody Section section) {
        sectionService.updateSection(id, section);
        return sectionService.getSection(id);
    }
    @DeleteMapping("/v2/sections/{id}")
    public void deleteSection(@PathVariable UUID id) {
        sectionService.deleteSection(id);
    }
}