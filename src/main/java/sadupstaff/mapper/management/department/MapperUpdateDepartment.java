package sadupstaff.mapper.management.department;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;
import sadupstaff.dto.management.department.DepartmentDTO;
import sadupstaff.dto.management.department.UpdateDepartmentDTO;
import sadupstaff.model.department.UpdateRequestDepartment;

@Component
@Mapper(componentModel = "spring")
public interface MapperUpdateDepartment {

    UpdateDepartmentDTO updateRequestToDTO(UpdateRequestDepartment updateRequestDepartment);

    UpdateDepartmentDTO departmentDTOToUpdateDepartmentDTO(DepartmentDTO DepartmentDTO);

    DepartmentDTO updateDepartmentDTOToDepartmentDTO(UpdateDepartmentDTO updateDepartmentDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDepartmentDTO(UpdateDepartmentDTO updateData, @MappingTarget UpdateDepartmentDTO updateDepartmentDTO);
}
