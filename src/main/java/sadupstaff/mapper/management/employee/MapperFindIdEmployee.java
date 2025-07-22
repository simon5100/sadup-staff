package sadupstaff.mapper.management.employee;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import sadupstaff.dto.management.employee.EmployeeDTO;
import sadupstaff.entity.management.Employee;
import sadupstaff.model.employee.ResponseEmployee;

@Component
@Mapper(componentModel = "spring")
public interface MapperFindIdEmployee {

    EmployeeDTO employeeToEmployeeDTO(Employee employee);

    ResponseEmployee employeeDTOToEmployeeResponse(EmployeeDTO employeeDTO);

}
