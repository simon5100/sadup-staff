package sadupstaff.service.section;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sadupstaff.dto.request.section.CreateSectionRequest;
import sadupstaff.dto.request.section.UpdateSectionRequest;
import sadupstaff.dto.response.SectionResponse;
import sadupstaff.dto.section.UpdateSectionDTO;
import sadupstaff.entity.district.District;
import sadupstaff.entity.district.Section;
import sadupstaff.mapper.section.MapperCreateSection;
import sadupstaff.mapper.section.MapperFindSection;
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

    private final MapperUpdateSection mapperUpdateSection;
    private final SectionRepository sectionRepository;
    private final DistrictServiceImpl districtService;
    private final MapperFindSection mapperFindSection;
    private final MapperCreateSection mapperCreateSection;

    @Override
    @Transactional
    public List<SectionResponse> getAllSection() {

        return sectionRepository.findAll().stream()
                .map(section -> mapperFindSection.entityToResponse(section))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SectionResponse getSectionById(UUID id) {
        Optional<Section> optionalSection = sectionRepository.findById(id);
        if(optionalSection.isPresent()) {
            return mapperFindSection.entityToResponse(optionalSection.get());
        }
        return null;
    }

    @Override
    public Section getSectionByIdForUpdate(UUID id) {
        Optional<Section> optionalSection = sectionRepository.findById(id);
        if(optionalSection.isPresent()) {
            return optionalSection.get();
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
    public SectionResponse saveSection(CreateSectionRequest createRequest) {
        Section section = mapperCreateSection.toEntity(createRequest);
        District district = districtService.getDistrictByName(createRequest.getDistrictName());
        section.setDistrict(district);
        section.setCreatedAt(LocalDateTime.now());
        section.setUpdatedAt(LocalDateTime.now());
        section = sectionRepository.save(section);

        return getSectionById(section.getId());
    }

    @Override
    @Transactional
    public SectionResponse updateSection(UUID id, UpdateSectionRequest updateRequest) {
        UpdateSectionDTO updateData = mapperUpdateSection.updateRequestToDTO(updateRequest);
        UpdateSectionDTO updateSectionOld = mapperUpdateSection
                .entityToUpdateSectionDTO(getSectionByIdForUpdate(id));
        mapperUpdateSection.update(updateData, updateSectionOld);
        Section section = mapperUpdateSection.updateSectionToEntity(updateSectionOld);
        section.setUpdatedAt(LocalDateTime.now());
        sectionRepository.save(section);

        return getSectionById(id);
    }

    @Override
    @Transactional
    public void deleteSection(UUID id) {
        sectionRepository.deleteById(id);
    }
}
