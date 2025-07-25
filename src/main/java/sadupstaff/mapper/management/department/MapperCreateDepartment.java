package sadupstaff.mapper.management.department;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import sadupstaff.dto.management.department.DepartmentDTO;
import sadupstaff.model.department.CreateRequestDepartment;

@Component
@Mapper(componentModel = "spring")
public interface MapperCreateDepartment {

    @Mapping(target = "id", ignore = true)
    DepartmentDTO createDepartmentToDTO(CreateRequestDepartment createRequestDepartment);
}
