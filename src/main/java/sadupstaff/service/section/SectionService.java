package sadupstaff.service.section;

import sadupstaff.dto.request.create.CreateSectionRequest;
import sadupstaff.dto.request.update.UpdateSectionRequest;
import sadupstaff.dto.response.SectionResponse;
import sadupstaff.entity.district.Section;
import java.util.List;
import java.util.UUID;

public interface SectionService {

    List<SectionResponse> getAllSection();

    SectionResponse getSectionById(UUID id);

    Section getSectionByIdForUpdate(UUID id);

    Section getSectionByName(String name);

    SectionResponse saveSection(CreateSectionRequest createRequest);

    SectionResponse updateSection(UUID id, UpdateSectionRequest updateRequest);

    void deleteSection(UUID id);
}
