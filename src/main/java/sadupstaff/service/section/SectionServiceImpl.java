package sadupstaff.service.section;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sadupstaff.dto.request.section.CreateRequestSection;
import sadupstaff.dto.request.section.UpdateRequestSection;
import sadupstaff.dto.response.ResponseSection;
import sadupstaff.dto.section.SectionDTO;
import sadupstaff.dto.section.UpdateSectionDTO;
import sadupstaff.entity.district.District;
import sadupstaff.entity.district.Section;
import sadupstaff.mapper.section.MapperCreateSection;
import sadupstaff.mapper.section.MapperFindSection;
import sadupstaff.mapper.section.MapperSection;
import sadupstaff.mapper.section.MapperUpdateSection;
import sadupstaff.repository.SectionRepository;
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
    private final MapperUpdateSection mapperUpdateSection;
    private final SectionRepository sectionRepository;
    private final DistrictServiceImpl districtService;
    private final MapperFindSection mapperFindSection;
    private final MapperCreateSection mapperCreateSection;

    @Override
    @Transactional
    public List<ResponseSection> getAllSection() {

        return sectionRepository.findAll().stream()
                .map(section -> mapperFindSection.entityToResponseSection(section))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ResponseSection getSectionById(UUID id) {
        Optional<Section> optionalSection = sectionRepository.findById(id);
        if(optionalSection.isPresent()) {
            return mapperFindSection.entityToResponseSection(optionalSection.get());
        }
        return null;
    }

    @Override
    public SectionDTO getSectionByIdForUpdate(UUID id) {
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
    public ResponseSection saveNewSection(CreateRequestSection createRequest) {
        Section section = mapperCreateSection.createSectionToEntity(createRequest);
        District district = districtService.getDistrictByName(createRequest.getDistrictName());
        section.setDistrict(district);
        section.setCreatedAt(LocalDateTime.now());
        section.setUpdatedAt(LocalDateTime.now());
        section = sectionRepository.save(section);

        return getSectionById(section.getId());
    }

    @Override
    @Transactional
    public ResponseSection updateSection(UUID id, UpdateRequestSection updateRequest) {
        UpdateSectionDTO updateData = mapperUpdateSection.updateRequestToDTO(updateRequest);
        SectionDTO sectionDTO = getSectionByIdForUpdate(id);
        UpdateSectionDTO updateSectionDTO = mapperUpdateSection
                .sectionDTOToUpdateSectionDTO(sectionDTO);
        mapperUpdateSection.update(updateData,updateSectionDTO);
        sectionDTO = mapperUpdateSection.updateSectionToSectionDTO(updateSectionDTO);
        Section section = mapperSection.toSection(sectionDTO);
        sectionRepository.save(section);

        return getSectionById(id);
    }

    @Override
    @Transactional
    public void deleteSection(UUID id) {
        sectionRepository.deleteById(id);
    }
}
