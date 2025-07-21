package sadupstaff.mapper.management.employee;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import sadupstaff.dto.management.EmployeeDTO;
import sadupstaff.entity.management.Employee;
import sadupstaff.model.employee.CreateRequestEmployee;

@Component
@Mapper(componentModel = "spring")
public interface MapperCreateEmployee {

    @Mapping(target = "id", ignore = true)
    EmployeeDTO toDto(CreateRequestEmployee createRequestEmployee);

    Employee toEntity(EmployeeDTO employeeDTO);

}
