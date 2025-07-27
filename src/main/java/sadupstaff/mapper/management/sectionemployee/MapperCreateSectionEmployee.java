package sadupstaff.mapper.management.sectionemployee;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sadupstaff.dto.sectionemployee.SectionEmployeeDTO;
import sadupstaff.model.sectionemployee.CreateRequestSectionEmployee;

@Mapper(componentModel = "spring")
public interface MapperCreateSectionEmployee {

    @Mapping(target = "id", ignore = true)
    SectionEmployeeDTO toDTO(CreateRequestSectionEmployee createRequestSectionEmployee);
}
