package sadupstaff.mapper.management.employee;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;
import sadupstaff.dto.management.EmployeeDTO;
import sadupstaff.entity.management.Employee;
import sadupstaff.model.employee.UpdateRequestEmployee;

@Component
@Mapper(componentModel = "spring")
public interface MapperUpdateEmployee {

    EmployeeDTO updateEmployeeToEmployeeDTO(UpdateRequestEmployee updateRequestEmployee);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEmployeeDTOToEmployee(EmployeeDTO updateEmployeeDTO, @MappingTarget EmployeeDTO employee);

}
