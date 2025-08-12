package sadupstaff.mapper.district;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sadupstaff.dto.request.district.CreateRequestDistrict;
import sadupstaff.entity.district.District;

@Mapper(componentModel = "spring")
public interface MapperCreateDistrict {

    @Mapping(target = "id", ignore = true)
    District toEntity(CreateRequestDistrict createRequestDistrict);
}
