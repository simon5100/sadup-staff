package sadupstaff.mapper.sectionemployee;

import org.mapstruct.*;
import sadupstaff.entity.district.SectionEmployee;
import sadupstaff.enums.PositionSectionEmployee;
import sadupstaff.dto.request.create.CreateSectionEmployeeRequest;

@Mapper(componentModel = "spring", uses = {PositionSectionEmployee.class})
public interface CreateSectionEmployeeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "section", ignore = true)
    SectionEmployee toEntity(CreateSectionEmployeeRequest createSectionEmployeeRequest);
}