package sadupstaff.service.sectionemployee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sadupstaff.dto.request.create.CreateSectionEmployeeRequest;
import sadupstaff.dto.request.update.UpdateSectionEmployeeRequest;
import sadupstaff.dto.response.SectionEmployeeResponse;
import sadupstaff.entity.district.Section;
import sadupstaff.entity.district.SectionEmployee;
import sadupstaff.exception.IdNotFoundException;
import sadupstaff.exception.employee.MaxEmployeeInDepartmentException;
import sadupstaff.exception.PositionOccupiedException;
import sadupstaff.exception.sectionemployee.MaxEmployeeInSectionException;
import sadupstaff.mapper.sectionemployee.CreateSectionEmployeeMapper;
import sadupstaff.mapper.sectionemployee.FindSectionEmployeeMapper;
import sadupstaff.mapper.sectionemployee.UpdateSectionEmployeeMapper;
import sadupstaff.repository.SectionEmployeeRepository;
import sadupstaff.service.section.SectionServiceImpl;
import java.time.LocalDateTime;
import java.util.List;
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
        SectionEmployee sectionEmployee = sectionEmployeeRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(id.toString()));

        return findSectionEmployeeMapper.entityToResponse(sectionEmployee);
    }

    @Override
    public SectionEmployee getSectionEmployeeForUpdate(UUID id) {
        return sectionEmployeeRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(id.toString()));
    }

    @Override
    @Transactional
    public SectionEmployeeResponse saveNewSectionEmployee(CreateSectionEmployeeRequest createRequest) {
        SectionEmployee sectionEmployee = createSectionEmployeeMapper.toEntity(createRequest);
        Section section = sectionService.getSectionByName(createRequest.getSectionName());

        if (section.getMaxNumberEmployeeSection() == section.getEmpsSect().size()) {
            throw new MaxEmployeeInSectionException(createRequest.getSectionName());
        }
        for (SectionEmployee sctemps: section.getEmpsSect()) {
            if (sectionEmployee.getPosition().equals(sctemps.getPosition())) {
                throw new PositionOccupiedException(createRequest.getPosition());
            }
        }
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
