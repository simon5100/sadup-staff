package sadupstaff.mapper.department;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import sadupstaff.dto.request.create.CreateDepartmentRequest;
import sadupstaff.entity.management.Department;
import sadupstaff.enums.DepartmentNameEnum;
import sadupstaff.exception.DepartmentNotFoundException;

@Mapper(componentModel = "spring")
public interface CreateDepartmentMapper {

    @Mapping(target = "name", ignore = true)
    Department toEntity(CreateDepartmentRequest createDepartmentRequest);

    @AfterMapping
    default void stringInEnum(CreateDepartmentRequest createDepartmentRequest, @MappingTarget Department department) {
        String name = createDepartmentRequest.getName().trim();
        for (DepartmentNameEnum value : DepartmentNameEnum.values()) {
            if(name.equalsIgnoreCase(value.getStringConvert())) {
                department.setName(value);
                return;
            }
        }
        throw new DepartmentNotFoundException(name);
    }
}