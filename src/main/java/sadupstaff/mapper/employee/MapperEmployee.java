package sadupstaff.mapper.employee;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import sadupstaff.dto.management.employee.EmployeeDTO;
import sadupstaff.entity.management.Employee;
import sadupstaff.mapper.department.MapperDepartment;

@Component
@Mapper(componentModel = "spring", uses = MapperDepartment.class)
public interface MapperEmployee {

    @Mapping(target = "departmentName", source = "department.name")
    EmployeeDTO toDTO(Employee employee);

    Employee toEmployee(EmployeeDTO employeeDTO);
}
