package sadupstaff.mapper.management.section;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sadupstaff.dto.section.SectionDTO;
import sadupstaff.entity.district.Section;
import sadupstaff.mapper.management.sectionemployee.MapperFindSectionEmployee;
import sadupstaff.mapper.management.sectionemployee.MapperSectionEmployee;

@Mapper(componentModel = "spring", uses = {MapperSectionEmployee.class, MapperFindSectionEmployee.class})
public interface MapperSection {

    @Mapping(target = "districtName", source = "district.name")
    SectionDTO toDTO(Section section);

    Section toSection(SectionDTO sectionDTO);
}
