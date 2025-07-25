package sadupstaff.mapper.management.employee;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import sadupstaff.dto.management.employee.EmployeeDTO;
import sadupstaff.model.employee.ResponseEmployee;

@Component
@Mapper(componentModel = "spring")
public interface MapperFindIdEmployee {

    ResponseEmployee employeeDTOToEmployeeResponse(EmployeeDTO employeeDTO);
}
