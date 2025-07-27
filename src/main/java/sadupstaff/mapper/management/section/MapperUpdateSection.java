package sadupstaff.mapper.management.section;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import sadupstaff.dto.section.SectionDTO;
import sadupstaff.dto.section.UpdateSectionDTO;
import sadupstaff.model.section.UpdateRequestSection;

@Mapper(componentModel = "spring")
public interface MapperUpdateSection {

    UpdateSectionDTO updateRequestToDTO(UpdateRequestSection updateRequest);

    UpdateSectionDTO sectionDTOToUpdateSectionDTO(SectionDTO sectionDTO);

    SectionDTO updateSectionToSectionDTO(UpdateSectionDTO updateSectionDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(UpdateSectionDTO updateData, @MappingTarget UpdateSectionDTO updateSectionDTO);
}
