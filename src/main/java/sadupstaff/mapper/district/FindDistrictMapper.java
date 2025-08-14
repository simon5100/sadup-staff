package sadupstaff.mapper.district;

import org.mapstruct.Mapper;
import sadupstaff.dto.response.DistrictResponse;
import sadupstaff.entity.district.District;
import sadupstaff.mapper.section.FindSectionMapper;

@Mapper(componentModel = "spring", uses = {FindSectionMapper.class})
public interface FindDistrictMapper {

    DistrictResponse entityToResponse(District district);
}
