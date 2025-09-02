package sadupstaff.mapper.employee;

import org.mapstruct.*;
import sadupstaff.dto.request.update.UpdateEmployeeRequest;
import sadupstaff.entity.management.Employee;
import sadupstaff.enums.PositionEmployee;

@Mapper(componentModel = "spring", uses = {PositionEmployee.class})
public interface UpdateEmployeeMapper {

    Employee toEntity(UpdateEmployeeRequest updateEmployeeRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEmployeeData(UpdateEmployeeRequest updateData, @MappingTarget Employee employeeOld);
}
