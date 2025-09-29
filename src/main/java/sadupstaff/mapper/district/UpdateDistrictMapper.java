package sadupstaff.mapper.district;

import org.mapstruct.*;
import sadupstaff.dto.request.update.UpdateDistrictRequest;
import sadupstaff.entity.district.District;
import sadupstaff.enums.DistrictName;
import sadupstaff.mapper.section.FindSectionMapper;

@Mapper(componentModel = "spring", uses = {FindSectionMapper.class, DistrictName.class})
public interface UpdateDistrictMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "maxNumberSection", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "sections", ignore = true)
    District toEntity(UpdateDistrictRequest updateRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "maxNumberSection", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "sections", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDistrictData(UpdateDistrictRequest updateData, @MappingTarget District districtOld);
}
