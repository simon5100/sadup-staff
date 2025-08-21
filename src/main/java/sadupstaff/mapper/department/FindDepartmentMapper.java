package sadupstaff.mapper.department;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;
import sadupstaff.dto.response.DepartmentResponse;
import sadupstaff.entity.management.Department;
import sadupstaff.enums.DepartmentNameEnum;
import sadupstaff.mapper.employee.FindEmployeeMapper;

@Component
@Mapper(componentModel = "spring", uses = {FindEmployeeMapper.class, DepartmentNameEnum.class})
public interface FindDepartmentMapper {

    @Mapping(target = "name", ignore = true)
    DepartmentResponse entityToResponse(Department department);

    @AfterMapping
    default void enumInString(@MappingTarget DepartmentResponse departmentResponse, Department department) {
        departmentResponse.setName(department.getName().getStringConvert());
    }
}
