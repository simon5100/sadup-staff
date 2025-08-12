package sadupstaff.mapper.employee;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import sadupstaff.dto.request.employee.CreateRequestEmployee;
import sadupstaff.entity.management.Employee;

@Component
@Mapper(componentModel = "spring")
public interface MapperCreateEmployee {

    @Mapping(target = "id", ignore = true)
    Employee toEntity(CreateRequestEmployee createRequestEmployee);
}
