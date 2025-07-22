package sadupstaff.mapper.management.employee;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import sadupstaff.dto.management.employee.EmployeeDTO;
import sadupstaff.dto.management.employee.UpdateEmployeeDTO;
import sadupstaff.model.employee.UpdateRequestEmployee;

@Component
@Mapper(componentModel = "spring")
public interface MapperUpdateEmployee {

    UpdateEmployeeDTO updateRequestEmployeetoUpdateEmployeeDTO(UpdateRequestEmployee updateRequestEmployee);

    UpdateEmployeeDTO EmployeeDTOToUpdateEmployeeDTO(EmployeeDTO employeeDTO);

    EmployeeDTO updateEmployeeDTOToEmployeeDTO(UpdateEmployeeDTO updateEmployeeDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEmployeeDTO(UpdateEmployeeDTO updateData, @MappingTarget UpdateEmployeeDTO updateEmployeeDTO);

}
