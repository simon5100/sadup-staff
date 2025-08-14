package sadupstaff.mapper.employee;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import sadupstaff.dto.request.update.UpdateEmployeeRequest;
import sadupstaff.entity.management.Employee;

@Component
@Mapper(componentModel = "spring")
public interface UpdateEmployeeMapper {

    Employee toEntity(UpdateEmployeeRequest updateEmployeeRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEmployeeData(UpdateEmployeeRequest updateData, @MappingTarget Employee employeeOld);
}
