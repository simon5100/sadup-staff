package sadupstaff.mapper.employee;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sadupstaff.dto.request.create.CreateEmployeeRequest;
import sadupstaff.entity.management.Employee;
import sadupstaff.enums.PositionEmployee;

@Mapper(componentModel = "spring", uses = {PositionEmployee.class})
public interface CreateEmployeeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "department", ignore = true)
    Employee toEntity(CreateEmployeeRequest createEmployeeRequest);
}
