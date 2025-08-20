package sadupstaff.mapper.district;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import sadupstaff.dto.request.create.CreateDistrictRequest;
import sadupstaff.entity.district.District;
import sadupstaff.enums.DistrictNameEnum;
import sadupstaff.exception.DistrictNotFoundException;

@Mapper(componentModel = "spring")
public interface CreateDistrictMapper {

    @Mapping(target = "name", ignore = true)
    District toEntity(CreateDistrictRequest createDistrictRequest);

    @AfterMapping
    default void stringInEnum(CreateDistrictRequest createDistrictRequest, @MappingTarget District district) {
        String name = createDistrictRequest.getName().trim();
        for (DistrictNameEnum value : DistrictNameEnum.values()) {
            if(name.equalsIgnoreCase(value.getStringConvert())) {
                district.setName(value);
                return;
            }
        }
        throw new DistrictNotFoundException(name);
    }
}
