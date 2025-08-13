package sadupstaff.mapper.employee;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import sadupstaff.dto.employee.UpdateEmployeeDTO;
import sadupstaff.dto.request.employee.UpdateEmployeeRequest;
import sadupstaff.entity.management.Employee;

@Component
@Mapper(componentModel = "spring")
public interface MapperUpdateEmployee {

    UpdateEmployeeDTO updateRequestEmployeeToUpdateEmployeeDTO(UpdateEmployeeRequest updateEmployeeRequest);

    UpdateEmployeeDTO entityToUpdateEmployeeDTO(Employee employee);

    Employee updateEmployeeDTOToEntity(UpdateEmployeeDTO updateEmployeeDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEmployeeData(UpdateEmployeeDTO updateData, @MappingTarget UpdateEmployeeDTO updateEmployeeDTO);
}
