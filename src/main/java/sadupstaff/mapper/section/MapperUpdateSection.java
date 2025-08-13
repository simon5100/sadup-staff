package sadupstaff.mapper.section;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import sadupstaff.dto.section.UpdateSectionDTO;
import sadupstaff.dto.request.section.UpdateSectionRequest;
import sadupstaff.entity.district.Section;

@Mapper(componentModel = "spring")
public interface MapperUpdateSection {

    UpdateSectionDTO updateRequestToDTO(UpdateSectionRequest updateRequest);

    UpdateSectionDTO entityToUpdateSectionDTO(Section section);

    Section updateSectionToEntity(UpdateSectionDTO updateSectionDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(UpdateSectionDTO updateData, @MappingTarget UpdateSectionDTO updateSectionDTO);
}
