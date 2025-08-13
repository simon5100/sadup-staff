package sadupstaff.mapper.sectionemployee;

import org.mapstruct.*;
import sadupstaff.entity.district.SectionEmployee;
import sadupstaff.enums.SectionEmployeeEnum;
import sadupstaff.dto.request.sectionemployee.CreateSectionEmployeeRequest;

@Mapper(componentModel = "spring", uses = {SectionEmployeeEnum.class})
public interface MapperCreateSectionEmployee {

    @Mapping(target = "position", ignore = true)
    SectionEmployee toEntity(CreateSectionEmployeeRequest createSectionEmployeeRequest);

    @AfterMapping
    default void stringInEnum(CreateSectionEmployeeRequest createSectionEmployeeRequest, @MappingTarget SectionEmployee sectionEmployee) {
        String position = createSectionEmployeeRequest.getPosition();
        for (SectionEmployeeEnum value : SectionEmployeeEnum.values()) {
            if(position.equalsIgnoreCase(value.getStringConvert())) {
                sectionEmployee.setPosition(value);
                break;
            }
        }
        new RuntimeException("Неверная позиция сотрудника");
    }
}
