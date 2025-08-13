package sadupstaff.mapper.employee;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import sadupstaff.dto.response.EmployeeResponse;
import sadupstaff.entity.management.Employee;

@Component
@Mapper(componentModel = "spring")
public interface MapperFindEmployee {

    @Mapping(target = "departmentName", source = "department.name")
    EmployeeResponse entityToResponse(Employee employee);
}
