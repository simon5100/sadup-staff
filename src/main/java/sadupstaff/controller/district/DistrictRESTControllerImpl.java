package sadupstaff.controller.district;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sadupstaff.dto.request.district.CreateDistrictRequest;
import sadupstaff.dto.response.DistrictResponse;
import sadupstaff.dto.request.district.UpdateDistrictRequest;
import sadupstaff.service.district.DistrictService;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class DistrictRESTControllerImpl implements DistrictRESTController {

    private final DistrictService districtService;

    public List<DistrictResponse> showDistricts() {
        return districtService.getAllDistrict();
    }

    public DistrictResponse showDistrict(@PathVariable UUID id) {
        return districtService.getDistrictById(id);
    }

    public DistrictResponse addDistrict (@RequestBody CreateDistrictRequest createRequest) {
        return districtService.saveDistrict(createRequest);
    }

    public DistrictResponse updateDistrict (@PathVariable UUID id, @RequestBody UpdateDistrictRequest updateReques) {
        return districtService.updateDistrict(id, updateReques);
    }

    public void deleteDistrict(@PathVariable UUID id) {
        districtService.deleteDistrict(id);
    }
}