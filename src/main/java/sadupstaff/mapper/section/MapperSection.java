package sadupstaff.mapper.section;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sadupstaff.dto.section.SectionDTO;
import sadupstaff.entity.district.Section;
import sadupstaff.mapper.sectionemployee.MapperFindSectionEmployee;
import sadupstaff.mapper.sectionemployee.MapperSectionEmployee;

@Mapper(componentModel = "spring", uses = {MapperSectionEmployee.class, MapperFindSectionEmployee.class})
public interface MapperSection {

    @Mapping(target = "districtName", source = "district.name")
    SectionDTO toDTO(Section section);

    Section toSection(SectionDTO sectionDTO);
}
