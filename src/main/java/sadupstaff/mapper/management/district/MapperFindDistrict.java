package sadupstaff.mapper.management.district;

import org.mapstruct.Mapper;
import sadupstaff.dto.district.DistrictDTO;
import sadupstaff.model.district.ResponseDistrict;

@Mapper(componentModel = "spring")
public interface MapperFindDistrict {

    ResponseDistrict DTOToResponseDistrict(DistrictDTO districtDTO);
}
