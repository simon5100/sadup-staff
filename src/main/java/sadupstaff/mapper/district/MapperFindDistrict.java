package sadupstaff.mapper.district;

import org.mapstruct.Mapper;
import sadupstaff.dto.response.DistrictResponse;
import sadupstaff.entity.district.District;
import sadupstaff.mapper.section.MapperFindSection;

@Mapper(componentModel = "spring", uses = {MapperFindSection.class})
public interface MapperFindDistrict {

    DistrictResponse entityToResponse(District district);
}
