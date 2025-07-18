package sadupstaff.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sadupstaff.entity.district.SectionEmployee;
import sadupstaff.service.sectionemployee.SectionEmployeeService;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SectionEmployeeRESTController {

    private final SectionEmployeeService sectionEmployeeService;

    @GetMapping("/v1/sectionEmployees")
    public List<SectionEmployee> showSections() {
        return sectionEmployeeService.getAllSectionEmployee();
    }

    @GetMapping("/v1/sectionEmployees/{id}")
    public SectionEmployee showSection(@PathVariable UUID id) {
        return sectionEmployeeService.getSectionEmployee(id);
    }

    @PostMapping("/v1/sectionEmployees")
    public SectionEmployee addSection (@RequestBody SectionEmployee sectionEmployee) {
        sectionEmployeeService.saveSectionEmployee(sectionEmployee);
        return sectionEmployee;
    }

    @PutMapping("/v1/sectionEmployees/{id}")
    public SectionEmployee updateSection (@PathVariable UUID id, @RequestBody SectionEmployee sectionEmployee) {
        sectionEmployeeService.updateSectionEmployee(id, sectionEmployee);
        return sectionEmployeeService.getSectionEmployee(id);
    }
    @DeleteMapping("/v1/sectionEmployees/{id}")
    public void deleteSection(@PathVariable UUID id) {
        sectionEmployeeService.deleteSectionEmployee(id);
    }
}