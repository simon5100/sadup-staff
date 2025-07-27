package sadupstaff.mapper.management.district;

import org.mapstruct.Mapper;
import sadupstaff.dto.district.DistrictDTO;
import sadupstaff.entity.district.District;
import sadupstaff.mapper.management.section.MapperFindSection;
import sadupstaff.mapper.management.section.MapperSection;

@Mapper(componentModel = "spring", uses = {MapperSection.class, MapperFindSection.class})
public interface MapperDistrict {

    DistrictDTO toDTO(District district);

    District toDistrict(DistrictDTO dto);
}
