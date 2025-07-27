package sadupstaff.mapper.management.department;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import sadupstaff.dto.management.department.DepartmentDTO;
import sadupstaff.entity.management.Department;
import sadupstaff.mapper.management.employee.MapperEmployee;
import sadupstaff.mapper.management.employee.MapperFindEmployee;

@Component
@Mapper(componentModel = "spring", uses = {MapperEmployee.class, MapperFindEmployee.class})
public interface MapperDepartment {

    DepartmentDTO toDTO(Department department);

    Department toDepartment(DepartmentDTO departmentDTO);
}
