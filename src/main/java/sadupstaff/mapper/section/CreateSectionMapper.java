package sadupstaff.mapper.section;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sadupstaff.dto.request.create.CreateSectionRequest;
import sadupstaff.entity.district.Section;

@Mapper(componentModel = "spring")
public interface CreateSectionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "district", ignore = true)
    @Mapping(target = "empsSect", ignore = true)
    Section toEntity(CreateSectionRequest createSectionRequest);
}
