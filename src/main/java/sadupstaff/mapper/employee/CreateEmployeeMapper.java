package sadupstaff.mapper.employee;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;
import sadupstaff.dto.request.create.CreateEmployeeRequest;
import sadupstaff.entity.management.Employee;
import sadupstaff.enums.PositionEmployeeEnum;
import sadupstaff.exception.PositionNotFoundException;

@Component
@Mapper(componentModel = "spring")
public interface CreateEmployeeMapper {

    @Mapping(target = "position", ignore = true)
    Employee toEntity(CreateEmployeeRequest createEmployeeRequest);

    @AfterMapping
    default void stringInEnum(CreateEmployeeRequest createEmployeeRequest, @MappingTarget Employee employee) {
        String position = createEmployeeRequest.getPosition().trim();
        for (PositionEmployeeEnum value : PositionEmployeeEnum.values()) {
            if(position.equalsIgnoreCase(value.getStringConvert())) {
                employee.setPosition(value);
                return;
            }
        }
        throw new PositionNotFoundException(position);
    }
}
