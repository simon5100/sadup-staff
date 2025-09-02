package sadupstaff.mapper.department;

import org.mapstruct.*;
import sadupstaff.dto.request.update.UpdateDepartmentRequest;
import sadupstaff.entity.management.Department;
import sadupstaff.enums.DepartmentName;
import sadupstaff.mapper.employee.FindEmployeeMapper;

@Mapper(componentModel = "spring", uses = {FindEmployeeMapper.class, DepartmentName.class})
public interface UpdateDepartmentMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDepartmentData(UpdateDepartmentRequest updateData, @MappingTarget Department departmentOld);
}
