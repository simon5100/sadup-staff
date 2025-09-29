package sadupstaff.mapper.district;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import sadupstaff.dto.request.create.CreateDistrictRequest;
import sadupstaff.entity.district.District;
import sadupstaff.enums.DistrictName;

@Mapper(componentModel = "spring", uses = {DistrictName.class})
public interface CreateDistrictMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "sections", ignore = true)
    District toEntity(CreateDistrictRequest createDistrictRequest);
}
