package sadupstaff.service.sectionemployee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sadupstaff.dto.request.sectionemployee.CreateSectionEmployeeRequest;
import sadupstaff.dto.request.sectionemployee.UpdateSectionEmployeeRequest;
import sadupstaff.dto.response.SectionEmployeeResponse;
import sadupstaff.dto.sectionemployee.UpdateSectionEmployeeDTO;
import sadupstaff.entity.district.Section;
import sadupstaff.entity.district.SectionEmployee;
import sadupstaff.mapper.sectionemployee.MapperCreateSectionEmployee;
import sadupstaff.mapper.sectionemployee.MapperFindSectionEmployee;
import sadupstaff.mapper.sectionemployee.MapperUpdateSectionEmployee;
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

    private final MapperUpdateSectionEmployee mapperUpdateSectionEmployee;
    private final SectionEmployeeRepository sectionEmployeeRepository;
    private final SectionServiceImpl sectionService;
    private final MapperFindSectionEmployee mapperFindSectionEmployee;
    private final MapperCreateSectionEmployee mapperCreateSectionEmployee;

    @Override
    @Transactional
    public List<SectionEmployeeResponse> getAllSectionEmployee() {

        return sectionEmployeeRepository.findAll().stream()
                .map(sectionEmployee -> mapperFindSectionEmployee.entityToResponse(sectionEmployee))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SectionEmployeeResponse getSectionEmployee(UUID id) {
        Optional<SectionEmployee> optionalSectionEmployee = sectionEmployeeRepository.findById(id);
        if (optionalSectionEmployee.isPresent()) {
            return mapperFindSectionEmployee.entityToResponse(optionalSectionEmployee.get());
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
        SectionEmployee sectionEmployee = mapperCreateSectionEmployee.toEntity(createRequest);
        Section section = sectionService.getSectionByName(createRequest.getSectionName());
        sectionEmployee.setSection(section);
        sectionEmployee.setCreatedAt(LocalDateTime.now());
        sectionEmployee.setUpdatedAt(LocalDateTime.now());
        sectionEmployee = sectionEmployeeRepository.save(sectionEmployee);

        return getSectionEmployee(sectionEmployee.getId());
    }

    @Override
    @Transactional
    public SectionEmployeeResponse updateSectionEmployee(UUID id, UpdateSectionEmployeeRequest updateRequest) {
        UpdateSectionEmployeeDTO updateData = mapperUpdateSectionEmployee.updateRequestToUpdateDTO(updateRequest);
        UpdateSectionEmployeeDTO updateSectionEmployeeOld = mapperUpdateSectionEmployee
                .entityToUpdateDTO(getSectionEmployeeForUpdate(id));
        mapperUpdateSectionEmployee.updateSectionEmployeeData(updateData, updateSectionEmployeeOld);
        SectionEmployee sectionEmployee = mapperUpdateSectionEmployee.updateDTOToEntity(updateSectionEmployeeOld);
        sectionEmployee.setUpdatedAt(LocalDateTime.now());
        sectionEmployeeRepository.save(sectionEmployee);

        return getSectionEmployee(id);
    }

    @Override
    @Transactional
    public void deleteSectionEmployee(UUID id) {
        sectionEmployeeRepository.deleteById(id);
    }
}
