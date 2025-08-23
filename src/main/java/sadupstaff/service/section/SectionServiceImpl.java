package sadupstaff.service.section;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sadupstaff.dto.request.create.CreateSectionRequest;
import sadupstaff.dto.request.update.UpdateSectionRequest;
import sadupstaff.dto.response.DepartmentResponse;
import sadupstaff.dto.response.SectionResponse;
import sadupstaff.entity.district.District;
import sadupstaff.entity.district.Section;
import sadupstaff.entity.management.Department;
import sadupstaff.entity.management.Employee;
import sadupstaff.exception.IdNotFoundException;
import sadupstaff.exception.PositionOccupiedException;
import sadupstaff.exception.department.DeleteDepartmentException;
import sadupstaff.exception.employee.MaxEmployeeInDepartmentException;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SectionServiceImpl implements SectionService{

    private final UpdateSectionMapper updateSectionMapper;
    private final SectionRepository sectionRepository;
    private final DistrictServiceImpl districtService;
    private final FindSectionMapper findSectionMapper;
    private final CreateSectionMapper createSectionMapper;

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
    public Section getSectionByIdForUpdate(UUID id) {
        return sectionRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(id.toString()));
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
        Section section = createSectionMapper.toEntity(createRequest);
        District district = districtService.getDistrictByName(createRequest.getDistrictName());

        if (district.getMaxNumberSection() == district.getSections().size()) {
            throw new MaxSectionInDistrictException(createRequest.getDistrictName().getStringConvert());
        }

        for (Section sect: district.getSections()) {
            if (sect.getName().equals(section.getName())) {
                throw new PositionOccupiedException(createRequest.getName());
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
        Section sectionOld = getSectionByIdForUpdate(id);
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
            throw new DeleteSectionException(section.getName());
        }

        sectionRepository.deleteById(id);
    }
}
