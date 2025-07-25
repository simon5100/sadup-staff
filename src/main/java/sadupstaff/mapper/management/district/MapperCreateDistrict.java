package sadupstaff.mapper.management.district;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sadupstaff.dto.district.DistrictDTO;
import sadupstaff.model.district.CreateRequestDistrict;

@Mapper(componentModel = "spring")
public interface MapperCreateDistrict {

    @Mapping(target = "id", ignore = true)
    DistrictDTO toDto(CreateRequestDistrict createRequestDistrict);
}
