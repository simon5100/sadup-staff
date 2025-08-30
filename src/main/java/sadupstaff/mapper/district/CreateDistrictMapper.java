package sadupstaff.mapper.district;

import org.mapstruct.Mapper;
import sadupstaff.dto.request.create.CreateDistrictRequest;
import sadupstaff.entity.district.District;
import sadupstaff.enums.DistrictNameEnum;

@Mapper(componentModel = "spring", uses = {DistrictNameEnum.class})
public interface CreateDistrictMapper {

    District toEntity(CreateDistrictRequest createDistrictRequest);
}
