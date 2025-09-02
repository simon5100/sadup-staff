package sadupstaff.mapper.employee;

import org.mapstruct.Mapper;
import sadupstaff.dto.request.create.CreateEmployeeRequest;
import sadupstaff.entity.management.Employee;
import sadupstaff.enums.PositionEmployee;

@Mapper(componentModel = "spring", uses = {PositionEmployee.class})
public interface CreateEmployeeMapper {

    Employee toEntity(CreateEmployeeRequest createEmployeeRequest);
}
