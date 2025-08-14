package sadupstaff.mapper.district;

import org.mapstruct.Mapper;
import sadupstaff.dto.request.create.CreateDistrictRequest;
import sadupstaff.entity.district.District;

@Mapper(componentModel = "spring")
public interface CreateDistrictMapper {

    District toEntity(CreateDistrictRequest createDistrictRequest);
}
