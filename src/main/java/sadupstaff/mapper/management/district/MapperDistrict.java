package sadupstaff.mapper.management.district;

import org.mapstruct.Mapper;
import sadupstaff.dto.district.DistrictDTO;
import sadupstaff.entity.district.District;

@Mapper(componentModel = "spring")
public interface MapperDistrict {

    DistrictDTO toDTO(District district);

    District toDistrict(DistrictDTO dto);
}
