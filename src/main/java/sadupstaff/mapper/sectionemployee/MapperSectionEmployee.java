package sadupstaff.mapper.sectionemployee;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sadupstaff.dto.sectionemployee.SectionEmployeeDTO;
import sadupstaff.entity.district.SectionEmployee;
import sadupstaff.enums.SectionEmployeeEnum;
import sadupstaff.mapper.section.MapperSection;

@Mapper(componentModel = "spring", uses = {MapperSection.class, SectionEmployeeEnum.class})
public interface MapperSectionEmployee {

    @Mapping(target = "sectionName", source = "section.name")
    SectionEmployeeDTO toDTO(SectionEmployee sectionEmployee);

    SectionEmployee toSectionEmployee(SectionEmployeeDTO sectionEmployeeDTO);

}
