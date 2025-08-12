package sadupstaff.mapper.section;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sadupstaff.dto.request.section.CreateRequestSection;
import sadupstaff.entity.district.Section;

@Mapper(componentModel = "spring")
public interface MapperCreateSection {

    @Mapping(target = "id", ignore = true)
    Section createSectionToEntity(CreateRequestSection createRequestSection);
}
