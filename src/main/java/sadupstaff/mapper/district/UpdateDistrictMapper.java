package sadupstaff.mapper.district;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import sadupstaff.dto.request.update.UpdateDistrictRequest;
import sadupstaff.entity.district.District;
import sadupstaff.mapper.section.FindSectionMapper;

@Mapper(componentModel = "spring", uses = {FindSectionMapper.class})
public interface UpdateDistrictMapper {

    District toEntity(UpdateDistrictRequest updateRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDistrictData(UpdateDistrictRequest updateData, @MappingTarget District districtOld);
}
