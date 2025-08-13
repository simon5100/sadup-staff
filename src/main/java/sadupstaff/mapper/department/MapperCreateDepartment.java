package sadupstaff.mapper.department;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import sadupstaff.dto.request.department.CreateDepartmentRequest;
import sadupstaff.entity.management.Department;

@Component
@Mapper(componentModel = "spring")
public interface MapperCreateDepartment {

    Department toEntity(CreateDepartmentRequest createDepartmentRequest);
}
