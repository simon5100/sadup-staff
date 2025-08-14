package sadupstaff.mapper.department;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import sadupstaff.dto.response.DepartmentResponse;
import sadupstaff.entity.management.Department;
import sadupstaff.mapper.employee.FindEmployeeMapper;

@Component
@Mapper(componentModel = "spring", uses = {FindEmployeeMapper.class})
public interface FindDepartmentMapper {

    DepartmentResponse entityToResponse(Department department);
}
