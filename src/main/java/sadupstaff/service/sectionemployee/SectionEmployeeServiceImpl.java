package sadupstaff.service.sectionemployee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sadupstaff.dto.request.create.CreateSectionEmployeeRequest;
import sadupstaff.dto.request.update.UpdateSectionEmployeeRequest;
import sadupstaff.dto.response.SectionEmployeeResponse;
import sadupstaff.entity.district.Section;
import sadupstaff.entity.district.SectionEmployee;
import sadupstaff.mapper.sectionemployee.CreateSectionEmployeeMapper;
import sadupstaff.mapper.sectionemployee.FindSectionEmployeeMapper;
import sadupstaff.mapper.sectionemployee.UpdateSectionEmployeeMapper;
import sadupstaff.repository.SectionEmployeeRepository;
import sadupstaff.service.section.SectionServiceImpl;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SectionEmployeeServiceImpl implements SectionEmployeeService {

    private final UpdateSectionEmployeeMapper updateSectionEmployeeMapper;
    private final SectionEmployeeRepository sectionEmployeeRepository;
    private final SectionServiceImpl sectionService;
    private final FindSectionEmployeeMapper findSectionEmployeeMapper;
    private final CreateSectionEmployeeMapper createSectionEmployeeMapper;

    @Override
    @Transactional
    public List<SectionEmployeeResponse> getAllSectionEmployee() {

        return sectionEmployeeRepository.findAll().stream()
                .map(sectionEmployee -> findSectionEmployeeMapper.entityToResponse(sectionEmployee))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SectionEmployeeResponse getSectionEmployee(UUID id) {
        Optional<SectionEmployee> optionalSectionEmployee = sectionEmployeeRepository.findById(id);
        if (optionalSectionEmployee.isPresent()) {
            return findSectionEmployeeMapper.entityToResponse(optionalSectionEmployee.get());
        }
        return null;
    }

    @Override
    public SectionEmployee getSectionEmployeeForUpdate(UUID id) {
        Optional<SectionEmployee> optionalSectionEmployee = sectionEmployeeRepository.findById(id);
        if (optionalSectionEmployee.isPresent()) {
            return optionalSectionEmployee.get();
        }
        return null;
    }

    @Override
    @Transactional
    public SectionEmployeeResponse saveNewSectionEmployee(CreateSectionEmployeeRequest createRequest) {
        SectionEmployee sectionEmployee = createSectionEmployeeMapper.toEntity(createRequest);
        Section section = sectionService.getSectionByName(createRequest.getSectionName());
        sectionEmployee.setSection(section);
        sectionEmployee.setCreatedAt(LocalDateTime.now());
        sectionEmployee.setUpdatedAt(LocalDateTime.now());
        sectionEmployee = sectionEmployeeRepository.save(sectionEmployee);

        return getSectionEmployee(sectionEmployee.getId());
    }

    @Override
    @Transactional
    public SectionEmployeeResponse updateSectionEmployee(UUID id, UpdateSectionEmployeeRequest updateData) {
        SectionEmployee sectionEmployeeOld = getSectionEmployeeForUpdate(id);
        updateSectionEmployeeMapper.updateSectionEmployeeData(updateData, sectionEmployeeOld);
        sectionEmployeeOld.setUpdatedAt(LocalDateTime.now());
        sectionEmployeeRepository.save(sectionEmployeeOld);

        return findSectionEmployeeMapper.entityToResponse(sectionEmployeeOld);
    }

    @Override
    @Transactional
    public void deleteSectionEmployee(UUID id) {
        sectionEmployeeRepository.deleteById(id);
    }
}
