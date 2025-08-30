package sadupstaff.mapper.department;

import org.mapstruct.Mapper;
import sadupstaff.dto.request.create.CreateDepartmentRequest;
import sadupstaff.entity.management.Department;
import sadupstaff.enums.DepartmentNameEnum;

@Mapper(componentModel = "spring", uses = {DepartmentNameEnum.class})
public interface CreateDepartmentMapper {

    Department toEntity(CreateDepartmentRequest createDepartmentRequest);

}