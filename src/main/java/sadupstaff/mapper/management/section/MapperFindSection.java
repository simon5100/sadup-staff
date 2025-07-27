package sadupstaff.mapper.management.section;

import org.mapstruct.Mapper;
import sadupstaff.dto.section.SectionDTO;
import sadupstaff.model.section.ResponseSection;

@Mapper(componentModel = "spring")
public interface MapperFindSection {

    ResponseSection DTOToResponseSection(SectionDTO sectionDTO);
}
