package sadupstaff.mapper.sectionemployee;

import org.mapstruct.*;
import sadupstaff.dto.response.SectionEmployeeResponse;
import sadupstaff.entity.district.SectionEmployee;

@Mapper(componentModel = "spring")
public interface MapperFindSectionEmployee {

    @Mapping(target = "position", ignore = true)
    @Mapping(target = "sectionName", source = "section.name")
    SectionEmployeeResponse entityToResponse(SectionEmployee sectionEmployee);

    @AfterMapping
    default void enumInString(@MappingTarget SectionEmployeeResponse sectionEmployeeResponse, SectionEmployee sectionEmployee) {
        sectionEmployeeResponse.setPosition(sectionEmployee.getPosition().getStringConvert());
    }
}
