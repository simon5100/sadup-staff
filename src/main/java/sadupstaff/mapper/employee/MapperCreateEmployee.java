package sadupstaff.mapper.employee;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import sadupstaff.dto.request.employee.CreateEmployeeRequest;
import sadupstaff.entity.management.Employee;

@Component
@Mapper(componentModel = "spring")
public interface MapperCreateEmployee {

    Employee toEntity(CreateEmployeeRequest createEmployeeRequest);
}
