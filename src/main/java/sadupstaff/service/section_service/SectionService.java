package sadupstaff.service.section_service;

import sadupstaff.entity.district.Section;

import java.util.List;
import java.util.UUID;

public interface SectionService {
    public List<Section> getAllSection();

    public Section getSection(UUID id);

    public void saveSection(Section section);

    public void deleteSection(UUID id);
}
