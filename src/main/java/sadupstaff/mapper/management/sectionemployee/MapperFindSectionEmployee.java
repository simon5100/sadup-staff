package sadupstaff.mapper.management.sectionemployee;

import org.mapstruct.*;
import sadupstaff.dto.sectionemployee.SectionEmployeeDTO;
import sadupstaff.model.sectionemployee.ResponseSectionEmployee;

@Mapper(componentModel = "spring")
public interface MapperFindSectionEmployee {

    @Mapping(target = "position", ignore = true)
    ResponseSectionEmployee DTOtoResponse(SectionEmployeeDTO sectionEmployeeDTO);

    @AfterMapping
    default void enumInString(@MappingTarget ResponseSectionEmployee responseSectionEmployee, SectionEmployeeDTO sectionEmployeeDTO) {
        responseSectionEmployee.setPosition(sectionEmployeeDTO.getPosition().getStringConvert());
    }
}
