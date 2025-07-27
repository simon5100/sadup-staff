package sadupstaff.service.sectionemployee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sadupstaff.dto.sectionemployee.SectionEmployeeDTO;
import sadupstaff.dto.sectionemployee.UpdateSectionEmployeeDTO;
import sadupstaff.entity.district.Section;
import sadupstaff.entity.district.SectionEmployee;
import sadupstaff.mapper.management.sectionemployee.MapperCreateSectionEmployee;
import sadupstaff.mapper.management.sectionemployee.MapperFindSectionEmployee;
import sadupstaff.mapper.management.sectionemployee.MapperSectionEmployee;
import sadupstaff.mapper.management.sectionemployee.MapperUpdateSectionEmployee;
import sadupstaff.repository.SectionEmployeeRepository;
import sadupstaff.service.UpdatingData;
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


    @Override
    @Transactional
    public List<SectionEmployeeDTO> getAllSectionEmployee() {

        return sectionEmployeeRepository.findAll().stream()
                .map(sectionEmployee -> mapperSectionEmployee.toDTO(sectionEmployee))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SectionEmployeeDTO getSectionEmployee(UUID id) {
        Optional<SectionEmployee> optionalSectionEmployee = sectionEmployeeRepository.findById(id);
        if (optionalSectionEmployee.isPresent()) {
            return mapperSectionEmployee.toDTO(optionalSectionEmployee.get());
        }
        return null;
    }

    @Override
    @Transactional
    public UUID saveSectionEmployee(SectionEmployeeDTO sectionEmployeeDTO) {
        SectionEmployee sectionEmployee = mapperSectionEmployee.toSectionEmployee(sectionEmployeeDTO);
        Section section = sectionService.getSectionByName(sectionEmployeeDTO.getSectionName());
        sectionEmployee.setSection(section);
        if (sectionEmployee.getCreatedAt() == null) {
            sectionEmployee.setCreatedAt(LocalDateTime.now());
        }
        sectionEmployee.setUpdatedAt(LocalDateTime.now());
        sectionEmployee = sectionEmployeeRepository.save(sectionEmployee);
        return sectionEmployee.getId();
    }

    @Override
    @Transactional
    public void updateSectionEmployee(UUID id, UpdateSectionEmployeeDTO updateData) {
        SectionEmployeeDTO sectionEmployeeDTO = getSectionEmployee(id);
        UpdateSectionEmployeeDTO updateSectionEmployeeDTO = mapperUpdateSectionEmployee
                .DTOToUpdateDTO(sectionEmployeeDTO);
        mapperUpdateSectionEmployee.update(updateData, updateSectionEmployeeDTO);
        sectionEmployeeDTO = mapperUpdateSectionEmployee.updateDTOToDTO(updateSectionEmployeeDTO);
        saveSectionEmployee(sectionEmployeeDTO);
    }

    @Override
    @Transactional
    public void deleteSectionEmployee(UUID id) {
        sectionEmployeeRepository.deleteById(id);
    }
}
