package sadupstaff.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sadupstaff.dto.district.UpdateDistrictDTO;
import sadupstaff.mapper.management.district.MapperCreateDistrict;
import sadupstaff.mapper.management.district.MapperDistrict;
import sadupstaff.mapper.management.district.MapperFindDistrict;
import sadupstaff.mapper.management.district.MapperUpdateDistrict;
import sadupstaff.model.district.CreateRequestDistrict;
import sadupstaff.model.district.ResponseDistrict;
import sadupstaff.model.district.UpdateRequestDistrict;
import sadupstaff.service.district.DistrictService;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DistrictRESTController {

    private final DistrictService districtService;
    private final MapperDistrict mapperDistrict;
    private final MapperCreateDistrict mapperCreateDistrict;
    private final MapperUpdateDistrict mapperUpdateDistrict;
    private final MapperFindDistrict mapperFindDistrict;

    @GetMapping("/v1/districts")
    public List<ResponseDistrict> showDistricts() {

        return districtService.getAllDistrict().stream()
                .map(districtDTO -> mapperFindDistrict.DTOToResponseDistrict(districtDTO))
                .collect(Collectors.toList());
    }

    @GetMapping("/v1/districts/{id}")
    public ResponseDistrict showDistrict(@PathVariable UUID id) {

        return mapperFindDistrict.DTOToResponseDistrict(districtService.getDistrictById(id));
    }

    @PostMapping("/v1/districts")
    public ResponseDistrict addDistrict (@RequestBody CreateRequestDistrict createRequest) {

        return showDistrict(districtService.saveDistrict(mapperCreateDistrict.toDto(createRequest)));
    }

    @PutMapping("/v1/districts/{id}")
    public ResponseDistrict updateDistrict (@PathVariable UUID id, @RequestBody UpdateRequestDistrict updateReques) {
        UpdateDistrictDTO updateDTO = mapperUpdateDistrict.updateRequestToDTO(updateReques);
        districtService.updateDistrict(id, updateDTO);
        return showDistrict(id);
    }

    @DeleteMapping("/v1/districts/{id}")
    public void deleteDistrict(@PathVariable UUID id) {
        districtService.deleteDistrict(id);
    }
}