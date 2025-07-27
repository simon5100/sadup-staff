package sadupstaff.mapper.management.sectionemployee;

import org.mapstruct.Mapper;
import sadupstaff.dto.sectionemployee.SectionEmployeeDTO;
import sadupstaff.model.sectionemployee.ResponseSectionEmployee;

@Mapper(componentModel = "spring")
public interface MapperFindSectionEmployee {

    ResponseSectionEmployee DTOtoResponse(SectionEmployeeDTO sectionEmployeeDTO);
}
