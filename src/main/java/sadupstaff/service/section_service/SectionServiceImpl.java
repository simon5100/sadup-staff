package sadupstaff.service.section_service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sadupstaff.entity.district.Section;
import sadupstaff.repository.DistrictRepository;
import sadupstaff.repository.SectionRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SectionServiceImpl implements SectionService{

    private final SectionRepository sectionRepository;

    private final DistrictRepository districtRepository;


    @Override
    public List<Section> getAllSection() {
        return sectionRepository.findAll();
    }

    @Override
    public Section getSection(UUID id) {
        Section section = null;

        Optional<Section> optionalSection =  sectionRepository.findById(id);
        if(optionalSection.isPresent()) {
            section = optionalSection.get();
        }

        return section;
    }

    @Override
    public void saveSection(Section section) {
        sectionRepository.save(section);
    }

    @Override
    public void deleteSection(UUID id) {
        sectionRepository.deleteById(id);
    }
}
