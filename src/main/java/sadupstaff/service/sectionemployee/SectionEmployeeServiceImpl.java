package sadupstaff.service.sectionemployee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sadupstaff.entity.district.SectionEmployee;
import sadupstaff.repository.SectionEmployeeRepository;
import sadupstaff.repository.SectionRepository;
import sadupstaff.service.UpdatingData;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SectionEmployeeServiceImpl implements SectionEmployeeService {

    private final SectionEmployeeRepository sectionEmployeeRepository;

    private final SectionRepository sectionRepository;

    private final UpdatingData updatingData;


    @Override
    @Transactional
    public List<SectionEmployee> getAllSectionEmployee() {
        return sectionEmployeeRepository.findAll();
    }

    @Override
    @Transactional
    public SectionEmployee getSectionEmployee(UUID id) {
        Optional<SectionEmployee> optionalSectionEmployee = sectionEmployeeRepository.findById(id);
        if (optionalSectionEmployee.isPresent()) {
            return optionalSectionEmployee.get();
        }
        return null;
    }

    @Override
    @Transactional
    public void saveSectionEmployee(SectionEmployee sectionEmployee) {
        sectionEmployeeRepository.save(sectionEmployee);
    }

    @Override
    @Transactional
    public void updateSectionEmployee(UUID id, SectionEmployee sectionEmployeeUpdate) {
        SectionEmployee sectionEmployee = getSectionEmployee(id);
        sectionEmployeeRepository.save(updatingData.updatingData(sectionEmployee, sectionEmployeeUpdate, SectionEmployee.class));
    }

    @Override
    @Transactional
    public void deleteSectionEmployee(UUID id) {
        sectionEmployeeRepository.deleteById(id);
    }
}
