package sadupstaff.mapper.section;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sadupstaff.dto.response.ResponseSection;
import sadupstaff.entity.district.Section;
import sadupstaff.mapper.sectionemployee.MapperFindSectionEmployee;

@Mapper(componentModel = "spring", uses = MapperFindSectionEmployee.class)
public interface MapperFindSection {

    @Mapping(target = "districtName", source = "district.name")
    ResponseSection entityToResponseSection(Section section);
}
