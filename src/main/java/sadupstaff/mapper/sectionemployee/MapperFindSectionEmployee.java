package sadupstaff.mapper.sectionemployee;

import org.mapstruct.*;
import sadupstaff.dto.response.ResponseSectionEmployee;
import sadupstaff.entity.district.SectionEmployee;

@Mapper(componentModel = "spring")
public interface MapperFindSectionEmployee {

    @Mapping(target = "position", ignore = true)
    @Mapping(target = "sectionName", source = "section.name")
    ResponseSectionEmployee entityToResponse(SectionEmployee sectionEmployee);

    @AfterMapping
    default void enumInString(@MappingTarget ResponseSectionEmployee responseSectionEmployee, SectionEmployee sectionEmployee) {
        responseSectionEmployee.setPosition(sectionEmployee.getPosition().getStringConvert());
    }
}
