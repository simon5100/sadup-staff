package sadupstaff.mapper.management.department;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import sadupstaff.dto.management.department.DepartmentDTO;
import sadupstaff.entity.management.Department;
import sadupstaff.mapper.management.employee.MapperEmployee;
import sadupstaff.service.department.DepartmentService;

@Component
@Mapper(componentModel = "spring", uses = {MapperEmployee.class, DepartmentService.class})
public interface MapperDepartment {

    DepartmentDTO toDTO(Department department);

    Department toDepartment(DepartmentDTO departmentDTO);

}
