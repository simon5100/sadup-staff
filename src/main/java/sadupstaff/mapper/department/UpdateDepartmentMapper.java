package sadupstaff.mapper.department;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import sadupstaff.dto.request.update.UpdateDepartmentRequest;
import sadupstaff.entity.management.Department;
import sadupstaff.enums.DepartmentNameEnum;
import sadupstaff.exception.DepartmentNotFoundException;
import sadupstaff.mapper.employee.FindEmployeeMapper;

@Component
@Mapper(componentModel = "spring", uses = {FindEmployeeMapper.class})
public interface UpdateDepartmentMapper {


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "name", ignore = true)
    void updateDepartmentData(UpdateDepartmentRequest updateData, @MappingTarget Department departmentOld);

    @BeforeMapping
    default void updateName(UpdateDepartmentRequest updateData, @MappingTarget Department department) {
        String name = updateData.getName();

        if (name != null) {
            name = name.trim();
            for (DepartmentNameEnum value : DepartmentNameEnum.values()) {
                if (name.equalsIgnoreCase(value.getStringConvert())) {
                    department.setName(value);
                    return;
                }
            }
            throw new DepartmentNotFoundException(name);
        }
    }
}
