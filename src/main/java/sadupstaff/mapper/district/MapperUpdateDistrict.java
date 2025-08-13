package sadupstaff.mapper.district;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import sadupstaff.dto.district.UpdateDistrictDTO;
import sadupstaff.dto.request.district.UpdateDistrictRequest;
import sadupstaff.entity.district.District;
import sadupstaff.mapper.section.MapperFindSection;

@Mapper(componentModel = "spring", uses = {MapperFindSection .class})
public interface MapperUpdateDistrict {

    UpdateDistrictDTO updateRequestToDTO(UpdateDistrictRequest updateRequest);

    UpdateDistrictDTO entityToUpdateDistrictDTO(District district);

    District updateDTOToEntity(UpdateDistrictDTO updateDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDistrictData(UpdateDistrictDTO updateDTD, @MappingTarget UpdateDistrictDTO updateDistrictDTO);
}
