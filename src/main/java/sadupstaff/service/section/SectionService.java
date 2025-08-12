package sadupstaff.service.section;

import sadupstaff.dto.request.section.CreateRequestSection;
import sadupstaff.dto.request.section.UpdateRequestSection;
import sadupstaff.dto.response.ResponseSection;
import sadupstaff.dto.section.SectionDTO;
import sadupstaff.entity.district.Section;
import java.util.List;
import java.util.UUID;

public interface SectionService {

    List<ResponseSection> getAllSection();

    ResponseSection getSectionById(UUID id);

    SectionDTO getSectionByIdForUpdate(UUID id);

    Section getSectionByName(String name);

    ResponseSection saveNewSection(CreateRequestSection createRequest);

    ResponseSection updateSection(UUID id, UpdateRequestSection updateRequest);

    void deleteSection(UUID id);
}
