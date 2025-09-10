package sadupstaff.mapper.sectionemployee;

import org.mapstruct.*;
import sadupstaff.dto.request.update.UpdateSectionEmployeeRequest;
import sadupstaff.entity.district.SectionEmployee;
import sadupstaff.enums.PositionSectionEmployee;

@Mapper(componentModel = "spring", uses = {PositionSectionEmployee.class})
public interface UpdateSectionEmployeeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "section", ignore = true)
    SectionEmployee toEntity(UpdateSectionEmployeeRequest updateSectionEmployeeRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "section", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSectionEmployeeData(UpdateSectionEmployeeRequest updateData, @MappingTarget SectionEmployee sectionEmployeeOld);
}
