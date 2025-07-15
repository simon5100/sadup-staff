package sadupstaff.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sadupstaff.entity.district.SectionEmployee;
import sadupstaff.service.section_employee_service.SectionEmployeeService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
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

    @PutMapping("/sectionEmployees")
    public SectionEmployee updateSection (@RequestBody SectionEmployee sectionEmployee) {
        sectionEmployeeService.saveSectionEmployee(sectionEmployee);

        return sectionEmployee;
    }
    @DeleteMapping("/sectionEmployees/{id}")
    public String deleteSection(@PathVariable UUID id) {
        SectionEmployee sectionEmployee =
                sectionEmployeeService.getSectionEmployee(id);

        sectionEmployeeService.deleteSectionEmployee(id);

        return sectionEmployee + " удален";
    }
}
