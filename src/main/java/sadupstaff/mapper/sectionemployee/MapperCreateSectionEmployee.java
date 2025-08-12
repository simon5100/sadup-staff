package sadupstaff.mapper.sectionemployee;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import sadupstaff.entity.district.SectionEmployee;
import sadupstaff.enums.SectionEmployeeEnum;
import sadupstaff.dto.request.sectionemployee.CreateRequestSectionEmployee;

@Mapper(componentModel = "spring", uses = {SectionEmployeeEnum.class})
public interface MapperCreateSectionEmployee {

    @Named("convert")
    default SectionEmployeeEnum convert(String position) {
        for (SectionEmployeeEnum value : SectionEmployeeEnum.values()) {
            if(position.equalsIgnoreCase(value.getStringConvert())) {
                return value;
            }
        }
        return null;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "position", target = "position", qualifiedByName = "convert")
    SectionEmployee toEntity(CreateRequestSectionEmployee createRequestSectionEmployee);
}
