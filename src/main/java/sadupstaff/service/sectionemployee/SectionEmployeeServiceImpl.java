package sadupstaff.service.sectionemployee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sadupstaff.dto.request.sectionemployee.CreateRequestSectionEmployee;
import sadupstaff.dto.request.sectionemployee.UpdateRequestSectionEmployee;
import sadupstaff.dto.response.ResponseSectionEmployee;
import sadupstaff.dto.sectionemployee.SectionEmployeeDTO;
import sadupstaff.dto.sectionemployee.UpdateSectionEmployeeDTO;
import sadupstaff.entity.district.Section;
import sadupstaff.entity.district.SectionEmployee;
import sadupstaff.mapper.sectionemployee.MapperCreateSectionEmployee;
import sadupstaff.mapper.sectionemployee.MapperFindSectionEmployee;
import sadupstaff.mapper.sectionemployee.MapperSectionEmployee;
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

    private final MapperSectionEmployee mapperSectionEmployee;
    private final MapperUpdateSectionEmployee mapperUpdateSectionEmployee;
    private final SectionEmployeeRepository sectionEmployeeRepository;
    private final SectionServiceImpl sectionService;
    private final MapperFindSectionEmployee mapperFindSectionEmployee;
    private final MapperCreateSectionEmployee mapperCreateSectionEmployee;

    @Override
    @Transactional
    public List<ResponseSectionEmployee> getAllSectionEmployee() {

        return sectionEmployeeRepository.findAll().stream()
                .map(sectionEmployee -> mapperFindSectionEmployee.entityToResponse(sectionEmployee))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ResponseSectionEmployee getSectionEmployee(UUID id) {
        Optional<SectionEmployee> optionalSectionEmployee = sectionEmployeeRepository.findById(id);
        if (optionalSectionEmployee.isPresent()) {
            return mapperFindSectionEmployee.entityToResponse(optionalSectionEmployee.get());
        }
        return null;
    }

    @Override
    public SectionEmployeeDTO getSectionEmployeeForUpdate(UUID id) {
        Optional<SectionEmployee> optionalSectionEmployee = sectionEmployeeRepository.findById(id);
        if (optionalSectionEmployee.isPresent()) {
            return mapperSectionEmployee.toDTO(optionalSectionEmployee.get());
        }
        return null;
    }

    @Override
    @Transactional
    public ResponseSectionEmployee saveNewSectionEmployee(CreateRequestSectionEmployee createRequest) {
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
    public ResponseSectionEmployee updateSectionEmployee(UUID id, UpdateRequestSectionEmployee updateRequest) {
        UpdateSectionEmployeeDTO updateData = mapperUpdateSectionEmployee.updateRequestToUpdateDTO(updateRequest);
        SectionEmployeeDTO sectionEmployeeDTO = getSectionEmployeeForUpdate(id);
        UpdateSectionEmployeeDTO updateSectionEmployeeDTO = mapperUpdateSectionEmployee
                .DTOToUpdateDTO(sectionEmployeeDTO);
        mapperUpdateSectionEmployee.update(updateData, updateSectionEmployeeDTO);
        sectionEmployeeDTO = mapperUpdateSectionEmployee.updateDTOToDTO(updateSectionEmployeeDTO);
        SectionEmployee sectionEmployee = mapperSectionEmployee.toSectionEmployee(sectionEmployeeDTO);
        sectionEmployeeRepository.save(sectionEmployee);

        return getSectionEmployee(id);
    }

    @Override
    @Transactional
    public void deleteSectionEmployee(UUID id) {
        sectionEmployeeRepository.deleteById(id);
    }
}
