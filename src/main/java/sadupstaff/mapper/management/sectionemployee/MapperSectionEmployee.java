package sadupstaff.mapper.management.sectionemployee;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sadupstaff.dto.sectionemployee.SectionEmployeeDTO;
import sadupstaff.entity.district.SectionEmployee;
import sadupstaff.mapper.management.section.MapperSection;

@Mapper(componentModel = "spring", uses = MapperSection.class)
public interface MapperSectionEmployee {

    @Mapping(target = "sectionName", source = "section.name")
    SectionEmployeeDTO toDTO(SectionEmployee sectionEmployee);

    SectionEmployee toSectionEmployee(SectionEmployeeDTO sectionEmployeeDTO);
}
