package sadupstaff.controller.district;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sadupstaff.dto.district.UpdateDistrictDTO;
import sadupstaff.mapper.district.MapperCreateDistrict;
import sadupstaff.mapper.district.MapperFindDistrict;
import sadupstaff.mapper.district.MapperUpdateDistrict;
import sadupstaff.model.district.CreateRequestDistrict;
import sadupstaff.model.district.ResponseDistrict;
import sadupstaff.model.district.UpdateRequestDistrict;
import sadupstaff.service.district.DistrictService;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class DistrictRESTControllerImpl implements DistrictRESTController {

    private final DistrictService districtService;
    private final MapperCreateDistrict mapperCreateDistrict;
    private final MapperUpdateDistrict mapperUpdateDistrict;
    private final MapperFindDistrict mapperFindDistrict;

    public List<ResponseDistrict> showDistricts() {

        return districtService.getAllDistrict().stream()
                .map(districtDTO -> mapperFindDistrict.DTOToResponseDistrict(districtDTO))
                .collect(Collectors.toList());
    }

    public ResponseDistrict showDistrict(@PathVariable UUID id) {

        return mapperFindDistrict.DTOToResponseDistrict(districtService.getDistrictById(id));
    }

    public ResponseDistrict addDistrict (@RequestBody CreateRequestDistrict createRequest) {

        return showDistrict(districtService.saveDistrict(mapperCreateDistrict.toDto(createRequest)));
    }

    public ResponseDistrict updateDistrict (@PathVariable UUID id, @RequestBody UpdateRequestDistrict updateReques) {
        UpdateDistrictDTO updateDTO = mapperUpdateDistrict.updateRequestToDTO(updateReques);
        districtService.updateDistrict(id, updateDTO);
        return showDistrict(id);
    }

    public void deleteDistrict(@PathVariable UUID id) {
        districtService.deleteDistrict(id);
    }
}