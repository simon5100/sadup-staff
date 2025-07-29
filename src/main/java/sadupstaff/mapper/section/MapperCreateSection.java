package sadupstaff.mapper.section;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sadupstaff.dto.section.SectionDTO;
import sadupstaff.model.section.CreateRequestSection;

@Mapper(componentModel = "spring")
public interface MapperCreateSection {

    @Mapping(target = "id", ignore = true)
    SectionDTO createSectionToDTO(CreateRequestSection createRequestSection);
}
