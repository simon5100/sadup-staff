package sadupstaff.mapper.section;

import org.mapstruct.Mapper;
import sadupstaff.dto.request.section.CreateSectionRequest;
import sadupstaff.entity.district.Section;

@Mapper(componentModel = "spring")
public interface MapperCreateSection {

    Section toEntity(CreateSectionRequest createSectionRequest);
}
