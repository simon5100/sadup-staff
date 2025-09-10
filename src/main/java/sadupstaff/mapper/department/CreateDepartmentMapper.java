package sadupstaff.mapper.department;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sadupstaff.dto.request.create.CreateDepartmentRequest;
import sadupstaff.entity.management.Department;
import sadupstaff.enums.DepartmentName;

@Mapper(componentModel = "spring", uses = {DepartmentName.class})
public interface CreateDepartmentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "emps", ignore = true)
    Department toEntity(CreateDepartmentRequest createDepartmentRequest);

}