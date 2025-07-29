package sadupstaff.mapper.sectionemployee;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import sadupstaff.dto.sectionemployee.SectionEmployeeDTO;
import sadupstaff.dto.sectionemployee.UpdateSectionEmployeeDTO;
import sadupstaff.model.sectionemployee.UpdateRequestSectionEmployee;

@Mapper(componentModel = "spring")
public interface MapperUpdateSectionEmployee {

    UpdateSectionEmployeeDTO updateRequestToUpdateDTO(UpdateRequestSectionEmployee updateRequestSectionEmployee);

    UpdateSectionEmployeeDTO DTOToUpdateDTO(SectionEmployeeDTO sectionEmployeeDTO);

    SectionEmployeeDTO updateDTOToDTO(UpdateSectionEmployeeDTO updateSectionEmployeeDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(UpdateSectionEmployeeDTO updateData, @MappingTarget UpdateSectionEmployeeDTO updateSectionEmployeeDTO);
}
