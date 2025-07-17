package sadupstaff.service.section_service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sadupstaff.entity.district.Section;
import sadupstaff.repository.DistrictRepository;
import sadupstaff.repository.SectionRepository;
import sadupstaff.service.UpdatingData;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SectionServiceImpl implements SectionService{

    private final SectionRepository sectionRepository;

    private final DistrictRepository districtRepository;

    private final UpdatingData updatingData;


    @Override
    @Transactional
    public List<Section> getAllSection() {
        return sectionRepository.findAll();
    }

    @Override
    @Transactional
    public Section getSection(UUID id) {
        Optional<Section> optionalSection = sectionRepository.findById(id);
        if(optionalSection.isPresent()) {
            return optionalSection.get();
        }
        return null;
    }

    @Override
    @Transactional
    public void saveSection(Section section) {
        sectionRepository.save(section);
    }

    @Override
    @Transactional
    public void updateSection(UUID id, Section sectionUpdate) {
        Section section = getSection(id);
        sectionRepository.save(updatingData.updatingData(section, sectionUpdate, Section.class));
    }

    @Override
    @Transactional
    public void deleteSection(UUID id) {
        sectionRepository.deleteById(id);
    }
}
