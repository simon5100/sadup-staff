package sadupstaff.service.section;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sadupstaff.dto.section.SectionDTO;
import sadupstaff.dto.section.UpdateSectionDTO;
import sadupstaff.entity.district.District;
import sadupstaff.entity.district.Section;
import sadupstaff.mapper.management.section.MapperCreateSection;
import sadupstaff.mapper.management.section.MapperFindSection;
import sadupstaff.mapper.management.section.MapperSection;
import sadupstaff.mapper.management.section.MapperUpdateSection;
import sadupstaff.repository.SectionRepository;
import sadupstaff.service.UpdatingData;
import sadupstaff.service.district.DistrictServiceImpl;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SectionServiceImpl implements SectionService{

    private final MapperSection mapperSection;
    private final MapperCreateSection mapperCreateSection;
    private final MapperUpdateSection mapperUpdateSection;
    private final MapperFindSection mapperFindSection;
    private final SectionRepository sectionRepository;
    private final DistrictServiceImpl districtService;
    private final UpdatingData updatingData;


    @Override
    @Transactional
    public List<SectionDTO> getAllSection() {

        return sectionRepository.findAll().stream()
                .map(section -> mapperSection.toDTO(section))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SectionDTO getSectionById(UUID id) {
        Optional<Section> optionalSection = sectionRepository.findById(id);
        if(optionalSection.isPresent()) {
            return mapperSection.toDTO(optionalSection.get());
        }
        return null;
    }

    @Override
    public Section getSectionByName(String name) {
        Optional<Section> optionalSection = Optional.ofNullable(sectionRepository.findSectionByName(name));
        if(optionalSection.isPresent()) {
            return optionalSection.get();
        }
        return null;
    }

    @Override
    @Transactional
    public UUID saveSection(SectionDTO sectionDTO) {
        Section section = mapperSection.toSection(sectionDTO);
        District district = districtService.getDistrictByName(sectionDTO.getDistrictName());
        section.setDistrict(district);
        if (section.getCreatedAt() == null) {
            section.setCreatedAt(LocalDateTime.now());
        }

        section.setUpdatedAt(LocalDateTime.now());
        section = sectionRepository.save(section);
        return section.getId();
    }

    @Override
    @Transactional
    public void updateSection(UUID id, UpdateSectionDTO updateData) {
        SectionDTO sectionDTO = getSectionById(id);
        UpdateSectionDTO updateSectionDTO = mapperUpdateSection
                .sectionDTOToUpdateSectionDTO(sectionDTO);
        mapperUpdateSection.update(updateData,updateSectionDTO);
        sectionDTO = mapperUpdateSection.updateSectionToSectionDTO(updateSectionDTO);
        saveSection(sectionDTO);
    }

    @Override
    @Transactional
    public void deleteSection(UUID id) {
        sectionRepository.deleteById(id);
    }
}
