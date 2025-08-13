package sadupstaff.mapper.district;

import org.mapstruct.Mapper;
import sadupstaff.dto.request.district.CreateDistrictRequest;
import sadupstaff.entity.district.District;

@Mapper(componentModel = "spring")
public interface MapperCreateDistrict {

    District toEntity(CreateDistrictRequest createDistrictRequest);
}
