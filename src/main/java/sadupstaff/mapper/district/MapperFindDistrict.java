package sadupstaff.mapper.district;

import org.mapstruct.Mapper;
import sadupstaff.dto.district.DistrictDTO;
import sadupstaff.dto.response.ResponseDistrict;
import sadupstaff.entity.district.District;
import sadupstaff.mapper.section.MapperFindSection;
import sadupstaff.mapper.section.MapperSection;

@Mapper(componentModel = "spring", uses = {MapperSection.class, MapperFindSection.class})
public interface MapperFindDistrict {

    ResponseDistrict entityToResponseDistrict(District district);
}
