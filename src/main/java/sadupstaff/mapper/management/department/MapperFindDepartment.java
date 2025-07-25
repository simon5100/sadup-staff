package sadupstaff.mapper.management.department;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import sadupstaff.dto.management.department.DepartmentDTO;
import sadupstaff.model.department.ResponseDepartment;

@Component
@Mapper(componentModel = "spring")
public interface MapperFindDepartment {

    ResponseDepartment DTOToResponseDepartment(DepartmentDTO departmentDTO);
}
