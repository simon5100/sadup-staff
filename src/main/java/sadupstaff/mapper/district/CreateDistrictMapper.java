package sadupstaff.mapper.district;

import org.mapstruct.Mapper;
import sadupstaff.dto.request.create.CreateDistrictRequest;
import sadupstaff.entity.district.District;
import sadupstaff.enums.DistrictName;

@Mapper(componentModel = "spring", uses = {DistrictName.class})
public interface CreateDistrictMapper {

    District toEntity(CreateDistrictRequest createDistrictRequest);
}
