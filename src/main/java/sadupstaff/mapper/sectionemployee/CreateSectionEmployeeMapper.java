package sadupstaff.mapper.sectionemployee;

import org.mapstruct.*;
import sadupstaff.entity.district.SectionEmployee;
import sadupstaff.enums.PositionSectionEmployeeEnum;
import sadupstaff.dto.request.create.CreateSectionEmployeeRequest;

@Mapper(componentModel = "spring", uses = {PositionSectionEmployeeEnum.class})
public interface CreateSectionEmployeeMapper {

    SectionEmployee toEntity(CreateSectionEmployeeRequest createSectionEmployeeRequest);
}