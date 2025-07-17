package sadupstaff.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sadupstaff.entity.district.SectionEmployee;
import sadupstaff.service.section_employee_service.SectionEmployeeService;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SectionEmployeeRESTController {

    private final SectionEmployeeService sectionEmployeeService;

    @GetMapping("/sectionEmployees")
    public List<SectionEmployee> showSections() {
        return sectionEmployeeService.getAllSectionEmployee();
    }

    @GetMapping("/sectionEmployees/{id}")
    public SectionEmployee showSection(@PathVariable UUID id) {
        return sectionEmployeeService.getSectionEmployee(id);
    }

    @PostMapping("/sectionEmployees")
    public SectionEmployee addSection (@RequestBody SectionEmployee sectionEmployee) {
        sectionEmployeeService.saveSectionEmployee(sectionEmployee);
        return sectionEmployee;
    }

    @PutMapping("/sectionEmployees/{id}")
    public SectionEmployee updateSection (@PathVariable UUID id, @RequestBody SectionEmployee sectionEmployee) {
        sectionEmployeeService.updateSectionEmployee(id, sectionEmployee);
        return sectionEmployeeService.getSectionEmployee(id);
    }
    @DeleteMapping("/sectionEmployees/{id}")
    public void deleteSection(@PathVariable UUID id) {
        sectionEmployeeService.deleteSectionEmployee(id);
    }
}