package sadupstaff.service.section;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sadupstaff.dto.request.create.CreateSectionRequest;
import sadupstaff.dto.request.update.UpdateSectionRequest;
import sadupstaff.dto.response.SectionResponse;
import sadupstaff.entity.district.District;
import sadupstaff.entity.district.Section;
import sadupstaff.exception.IdNotFoundException;
import sadupstaff.exception.IncorrectFormatPersonalNumberException;
import sadupstaff.exception.PositionOccupiedException;
import sadupstaff.exception.SectionNotFoundByPersonelNumberException;
import sadupstaff.exception.section.DeleteSectionException;
import sadupstaff.exception.section.MaxSectionInDistrictException;
import sadupstaff.mapper.section.CreateSectionMapper;
import sadupstaff.mapper.section.FindSectionMapper;
import sadupstaff.mapper.section.UpdateSectionMapper;
import sadupstaff.repository.SectionRepository;
import sadupstaff.service.district.DistrictServiceImpl;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SectionServiceImpl implements SectionService{

    private final UpdateSectionMapper updateSectionMapper;
    private final SectionRepository sectionRepository;
    private final DistrictServiceImpl districtService;
    private final FindSectionMapper findSectionMapper;
    private final CreateSectionMapper createSectionMapper;
    private final Pattern sectionPersonalNumbers = Pattern.compile("54MS0[0-1]\\d{2}");

    @Override
    @Transactional
    public List<SectionResponse> getAllSection() {

        return sectionRepository.findAll().stream()
                .map(section -> findSectionMapper.entityToResponse(section))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SectionResponse getSectionById(UUID id) {
        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(id.toString()));

        return findSectionMapper.entityToResponse(section);
    }

    @Override
    public Section getSectionByPersonelNumber(String personalNumber) {
        if (!sectionPersonalNumbers.matcher(personalNumber).find()) {
            throw new IncorrectFormatPersonalNumberException(personalNumber);
        }

        Optional<Section> optionalSection = Optional.ofNullable(sectionRepository.findSectionByPersonelNumber(personalNumber));
        if(optionalSection.isPresent()) {
            return optionalSection.get();
        }
        throw new SectionNotFoundByPersonelNumberException(personalNumber);
    }

    @Override
    @Transactional
    public SectionResponse saveSection(CreateSectionRequest createRequest) {
        if (!sectionPersonalNumbers.matcher(createRequest.getPersonelNumber()).find()) {
            throw new IncorrectFormatPersonalNumberException(createRequest.getPersonelNumber());
        }

        Section section = createSectionMapper.toEntity(createRequest);
        District district = districtService.getDistrictByName(createRequest.getDistrictName());

        if (district.getMaxNumberSection() == district.getSections().size()) {
            throw new MaxSectionInDistrictException(createRequest.getDistrictName().getStringConvert());
        }

        for (Section sect: district.getSections()) {
            if (sect.getPersonelNumber().equals(section.getPersonelNumber())) {
                throw new PositionOccupiedException(String.valueOf(createRequest.getPersonelNumber()));
            }
        }

        section.setDistrict(district);
        section.setCreatedAt(LocalDateTime.now());
        section.setUpdatedAt(LocalDateTime.now());
        section = sectionRepository.save(section);

        return getSectionById(section.getId());
    }

    @Override
    @Transactional
    public SectionResponse updateSection(UUID id, UpdateSectionRequest updateData) {
        Section sectionOld = sectionRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(id.toString()));

        if (updateData.getPersonelNumber() != null && sectionRepository.existsSectionByPersonelNumber((updateData.getPersonelNumber()))) {
            throw new PositionOccupiedException(String.valueOf(updateData.getPersonelNumber()));
        }

        updateSectionMapper.update(updateData, sectionOld);
        sectionOld.setUpdatedAt(LocalDateTime.now());
        sectionRepository.save(sectionOld);

        return findSectionMapper.entityToResponse(sectionOld);
    }

    @Override
    @Transactional
    public void deleteSection(UUID id) {
        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(id.toString()));
        if (!section.getEmpsSect().isEmpty()) {
            throw new DeleteSectionException(section.getNumber());
        }

        sectionRepository.deleteById(id);
    }
}
