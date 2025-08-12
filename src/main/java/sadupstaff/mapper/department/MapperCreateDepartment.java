package sadupstaff.mapper.department;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import sadupstaff.dto.request.department.CreateRequestDepartment;
import sadupstaff.entity.management.Department;

@Component
@Mapper(componentModel = "spring")
public interface MapperCreateDepartment {

    @Mapping(target = "id", ignore = true)
    Department createDepartmentToEntity(CreateRequestDepartment createRequestDepartment);
}
