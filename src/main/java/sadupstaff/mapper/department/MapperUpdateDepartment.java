package sadupstaff.mapper.department;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import sadupstaff.dto.department.UpdateDepartmentDTO;
import sadupstaff.dto.request.department.UpdateDepartmentRequest;
import sadupstaff.entity.management.Department;
import sadupstaff.mapper.employee.MapperEmployee;
import sadupstaff.mapper.employee.MapperFindEmployee;

@Component
@Mapper(componentModel = "spring", uses = {MapperEmployee.class, MapperFindEmployee.class})
public interface MapperUpdateDepartment {

    UpdateDepartmentDTO updateRequestToDTO(UpdateDepartmentRequest updateDepartmentRequest);

    UpdateDepartmentDTO departmentToUpdateDepartmentDTO(Department Department);

    Department updateDepartmentDTOToDepartment(UpdateDepartmentDTO updateDepartmentDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDepartmentData(UpdateDepartmentDTO updateData, @MappingTarget UpdateDepartmentDTO updateDepartmentDTO);
}
