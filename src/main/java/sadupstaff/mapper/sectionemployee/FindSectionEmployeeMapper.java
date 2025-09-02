package sadupstaff.mapper.sectionemployee;

import org.mapstruct.*;
import sadupstaff.dto.response.SectionEmployeeResponse;
import sadupstaff.entity.district.SectionEmployee;
import sadupstaff.mapper.section.FindSectionMapper;

@Mapper(componentModel = "spring", uses = {FindSectionMapper.class})
public interface FindSectionEmployeeMapper {

    @Mapping(target = "position", ignore = true)
    @Mapping(target = "sectionName", source = "section.name")
    SectionEmployeeResponse entityToResponse(SectionEmployee sectionEmployee);

    @AfterMapping
    default void enumInString(@MappingTarget SectionEmployeeResponse sectionEmployeeResponse, SectionEmployee sectionEmployee) {
        sectionEmployeeResponse.setPosition(sectionEmployee.getPosition().getStringConvert());
    }
}
