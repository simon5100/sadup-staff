package sadupstaff.mapper.management.district;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import sadupstaff.dto.district.DistrictDTO;
import sadupstaff.dto.district.UpdateDistrictDTO;
import sadupstaff.model.district.UpdateRequestDistrict;

@Mapper(componentModel = "spring")
public interface MapperUpdateDistrict {

    UpdateDistrictDTO updateRequestToDTO(UpdateRequestDistrict updateRequest);

    UpdateDistrictDTO toDTO(DistrictDTO districtDTO);

    DistrictDTO updateDTOToDTO(UpdateDistrictDTO updateDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDistrictDTO(UpdateDistrictDTO updateDTD, @MappingTarget UpdateDistrictDTO updateDistrictDTO);
}
