package sadupstaff.mapper.department;

import org.mapstruct.Mapper;
import sadupstaff.dto.request.create.CreateDepartmentRequest;
import sadupstaff.entity.management.Department;
import sadupstaff.enums.DepartmentName;

@Mapper(componentModel = "spring", uses = {DepartmentName.class})
public interface CreateDepartmentMapper {

    Department toEntity(CreateDepartmentRequest createDepartmentRequest);

}