package sadupstaff.mapper.department;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import sadupstaff.dto.request.update.UpdateDepartmentRequest;
import sadupstaff.entity.management.Department;
import sadupstaff.enums.DepartmentNameEnum;
import sadupstaff.mapper.employee.FindEmployeeMapper;

@Component
@Mapper(componentModel = "spring", uses = {FindEmployeeMapper.class, DepartmentNameEnum.class})
public interface UpdateDepartmentMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDepartmentData(UpdateDepartmentRequest updateData, @MappingTarget Department departmentOld);
}
