package sadupstaff.mapper.department;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import sadupstaff.dto.department.DepartmentDTO;
import sadupstaff.entity.management.Department;
import sadupstaff.mapper.employee.MapperEmployee;
import sadupstaff.mapper.employee.MapperFindEmployee;

@Component
@Mapper(componentModel = "spring", uses = {MapperEmployee.class, MapperFindEmployee.class})
public interface MapperDepartment {

    DepartmentDTO toDTO(Department department);

    Department toDepartment(DepartmentDTO departmentDTO);
}
