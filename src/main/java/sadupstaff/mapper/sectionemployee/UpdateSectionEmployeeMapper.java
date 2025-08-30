package sadupstaff.mapper.sectionemployee;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import sadupstaff.dto.request.update.UpdateSectionEmployeeRequest;
import sadupstaff.entity.district.SectionEmployee;
import sadupstaff.enums.PositionSectionEmployeeEnum;

@Mapper(componentModel = "spring", uses = {PositionSectionEmployeeEnum.class})
public interface UpdateSectionEmployeeMapper {

    SectionEmployee toEntity(UpdateSectionEmployeeRequest updateSectionEmployeeRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSectionEmployeeData(UpdateSectionEmployeeRequest updateData, @MappingTarget SectionEmployee sectionEmployeeOld);
}
