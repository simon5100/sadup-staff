package sadupstaff.mapper.sectionemployee;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import sadupstaff.dto.sectionemployee.UpdateSectionEmployeeDTO;
import sadupstaff.dto.request.sectionemployee.UpdateSectionEmployeeRequest;
import sadupstaff.entity.district.SectionEmployee;

@Mapper(componentModel = "spring")
public interface MapperUpdateSectionEmployee {

    UpdateSectionEmployeeDTO updateRequestToUpdateDTO(UpdateSectionEmployeeRequest updateSectionEmployeeRequest);

    UpdateSectionEmployeeDTO entityToUpdateDTO(SectionEmployee sectionEmployee);

    SectionEmployee updateDTOToEntity(UpdateSectionEmployeeDTO updateSectionEmployeeDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSectionEmployeeData(UpdateSectionEmployeeDTO updateData, @MappingTarget UpdateSectionEmployeeDTO updateSectionEmployeeDTO);
}
