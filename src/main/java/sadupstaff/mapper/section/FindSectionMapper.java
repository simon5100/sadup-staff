package sadupstaff.mapper.section;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sadupstaff.dto.response.SectionResponse;
import sadupstaff.entity.district.Section;
import sadupstaff.mapper.sectionemployee.FindSectionEmployeeMapper;

@Mapper(componentModel = "spring", uses = {FindSectionEmployeeMapper.class})
public interface FindSectionMapper {

    @Mapping(target = "districtName", source = "district.name.stringConvert")
    SectionResponse entityToResponse(Section section);
}
