package sadupstaff.service.section;

import sadupstaff.dto.section.SectionDTO;
import sadupstaff.dto.section.UpdateSectionDTO;
import sadupstaff.entity.district.Section;

import java.util.List;
import java.util.UUID;

public interface SectionService {

    public List<SectionDTO> getAllSection();

    public SectionDTO getSectionById(UUID id);

    public Section getSectionByName(String name);

    public UUID saveSection(SectionDTO sectionDTO);

    public void updateSection(UUID id, UpdateSectionDTO updateSectionDTO);

    public void deleteSection(UUID id);
}
