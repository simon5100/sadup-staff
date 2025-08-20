package sadupstaff.mapper.sectionemployee;

import org.mapstruct.*;
import sadupstaff.entity.district.SectionEmployee;
import sadupstaff.enums.PositionSectionEmployeeEnum;
import sadupstaff.dto.request.create.CreateSectionEmployeeRequest;
import sadupstaff.exception.PositionNotFoundException;

@Mapper(componentModel = "spring", uses = {PositionSectionEmployeeEnum.class})
public interface CreateSectionEmployeeMapper {

    @Mapping(target = "position", ignore = true)
    SectionEmployee toEntity(CreateSectionEmployeeRequest createSectionEmployeeRequest);

    @AfterMapping
    default void stringInEnum(CreateSectionEmployeeRequest createSectionEmployeeRequest, @MappingTarget SectionEmployee sectionEmployee) {
        String position = createSectionEmployeeRequest.getPosition().trim();
        for (PositionSectionEmployeeEnum value : PositionSectionEmployeeEnum.values()) {
            if(position.equalsIgnoreCase(value.getStringConvert())) {
                sectionEmployee.setPosition(value);
                return;
            }
        }
        throw new PositionNotFoundException(position);
    }
}