package sadupstaff.mapper.employee;

import org.mapstruct.*;
import sadupstaff.dto.request.update.UpdateEmployeeRequest;
import sadupstaff.entity.management.Employee;
import sadupstaff.enums.PositionEmployee;

@Mapper(componentModel = "spring", uses = {PositionEmployee.class})
public interface UpdateEmployeeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "department", ignore = true)
    Employee toEntity(UpdateEmployeeRequest updateEmployeeRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "department", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEmployeeData(UpdateEmployeeRequest updateData, @MappingTarget Employee employeeOld);
}
