package sadupstaff.mapper.section;

import org.mapstruct.*;
import sadupstaff.dto.request.update.UpdateSectionRequest;
import sadupstaff.entity.district.Section;

@Mapper(componentModel = "spring")
public interface UpdateSectionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "maxNumberEmployeeSection", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "district", ignore = true)
    @Mapping(target = "empsSect", ignore = true)
    Section toEntity(UpdateSectionRequest updateRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "maxNumberEmployeeSection", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "district", ignore = true)
    @Mapping(target = "empsSect", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(UpdateSectionRequest updateData, @MappingTarget Section sectionOld);
}
