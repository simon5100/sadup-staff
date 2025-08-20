package sadupstaff.mapper.employee;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;
import sadupstaff.dto.response.EmployeeResponse;
import sadupstaff.entity.management.Employee;

@Component
@Mapper(componentModel = "spring")
public interface FindEmployeeMapper {


    @Mapping(target = "position", ignore = true)
    @Mapping(target = "departmentName", source = "department.name.stringConvert")
    EmployeeResponse entityToResponse(Employee employee);

    @AfterMapping
    default void enumInString(@MappingTarget EmployeeResponse employeeResponse, Employee employee) {
        employeeResponse.setPosition(employee.getPosition().getStringConvert());
    }
}
