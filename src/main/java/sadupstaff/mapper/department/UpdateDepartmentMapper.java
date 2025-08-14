package sadupstaff.mapper.department;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import sadupstaff.dto.request.update.UpdateDepartmentRequest;
import sadupstaff.entity.management.Department;
import sadupstaff.mapper.employee.FindEmployeeMapper;

@Component
@Mapper(componentModel = "spring", uses = {FindEmployeeMapper.class})
public interface UpdateDepartmentMapper {

    Department toEntity(UpdateDepartmentRequest updateDepartmentRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDepartmentData(UpdateDepartmentRequest updateData, @MappingTarget Department departmentOld);
}
