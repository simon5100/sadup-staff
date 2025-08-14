package sadupstaff.mapper.section;

import org.mapstruct.Mapper;
import sadupstaff.dto.request.create.CreateSectionRequest;
import sadupstaff.entity.district.Section;

@Mapper(componentModel = "spring")
public interface CreateSectionMapper {

    Section toEntity(CreateSectionRequest createSectionRequest);
}
