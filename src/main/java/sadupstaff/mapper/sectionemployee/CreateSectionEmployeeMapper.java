package sadupstaff.mapper.sectionemployee;

import org.mapstruct.*;
import sadupstaff.entity.district.SectionEmployee;
import sadupstaff.enums.PositionSectionEmployee;
import sadupstaff.dto.request.create.CreateSectionEmployeeRequest;

@Mapper(componentModel = "spring", uses = {PositionSectionEmployee.class})
public interface CreateSectionEmployeeMapper {

    SectionEmployee toEntity(CreateSectionEmployeeRequest createSectionEmployeeRequest);
}