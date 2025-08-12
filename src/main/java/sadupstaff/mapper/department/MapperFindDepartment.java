package sadupstaff.mapper.department;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import sadupstaff.dto.response.ResponseDepartment;
import sadupstaff.entity.management.Department;

@Component
@Mapper(componentModel = "spring")
public interface MapperFindDepartment {

    ResponseDepartment entityToResponseDepartment(Department department);
}
