package sadupstaff.mapper.district;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import sadupstaff.dto.response.DistrictResponse;
import sadupstaff.entity.district.District;
import sadupstaff.mapper.section.FindSectionMapper;

@Mapper(componentModel = "spring", uses = {FindSectionMapper.class})
public interface FindDistrictMapper {


    @Mapping(target = "name", ignore = true)
    DistrictResponse entityToResponse(District district);

    @AfterMapping
    default void enumInString(@MappingTarget DistrictResponse districtResponse, District district) {
        districtResponse.setName(district.getName().getStringConvert());
    }
}
