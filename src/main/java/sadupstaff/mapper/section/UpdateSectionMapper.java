package sadupstaff.mapper.section;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import sadupstaff.dto.request.update.UpdateSectionRequest;
import sadupstaff.entity.district.Section;

@Mapper(componentModel = "spring")
public interface UpdateSectionMapper {

    Section toEntity(UpdateSectionRequest updateRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(UpdateSectionRequest updateData, @MappingTarget Section sectionOld);
}
