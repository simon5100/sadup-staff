package sadupstaff.service.section_employee_service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sadupstaff.entity.district.SectionEmployee;
import sadupstaff.repository.SectionEmployeeRepository;
import sadupstaff.repository.SectionRepository;
import sadupstaff.service.section_service.SectionService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SectionEmployeeServiceImpl implements SectionEmployeeService {

    private final SectionEmployeeRepository sectionEmployeeRepository;

    private final SectionRepository sectionRepository;


    @Override
    @Transactional
    public List<SectionEmployee> getAllSectionEmployee() {
        return sectionEmployeeRepository.findAll();
    }

    @Override
    @Transactional
    public SectionEmployee getSectionEmployee(UUID id) {
        SectionEmployee sectionEmployee = null;
        Optional<SectionEmployee> optionalSectionEmployee =
                sectionEmployeeRepository.findById(id);

        if (optionalSectionEmployee.isPresent()) {
            sectionEmployee = optionalSectionEmployee.get();
        }

        return sectionEmployee;
    }

    @Override
    @Transactional
    public void saveSectionEmployee(SectionEmployee sectionEmployee) {
        sectionEmployeeRepository.save(sectionEmployee);
    }

    @Override
    @Transactional
    public void deleteSectionEmployee(UUID id) {
        sectionEmployeeRepository.deleteById(id);

    }
}
