package sadupstaff.mapper.employee;

import org.mapstruct.Mapper;
import sadupstaff.dto.request.create.CreateEmployeeRequest;
import sadupstaff.entity.management.Employee;
import sadupstaff.enums.PositionEmployeeEnum;

@Mapper(componentModel = "spring", uses = {PositionEmployeeEnum.class})
public interface CreateEmployeeMapper {

    Employee toEntity(CreateEmployeeRequest createEmployeeRequest);
}
