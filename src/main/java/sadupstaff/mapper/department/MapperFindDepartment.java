package sadupstaff.mapper.department;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import sadupstaff.dto.response.DepartmentResponse;
import sadupstaff.entity.management.Department;
import sadupstaff.mapper.employee.MapperFindEmployee;

@Component
@Mapper(componentModel = "spring", uses = {MapperFindEmployee.class})
public interface MapperFindDepartment {

    DepartmentResponse entityToResponse(Department department);
}
