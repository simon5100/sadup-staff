package sadupstaff.controller.district;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sadupstaff.dto.request.district.CreateRequestDistrict;
import sadupstaff.dto.response.ResponseDistrict;
import sadupstaff.dto.request.district.UpdateRequestDistrict;
import sadupstaff.service.district.DistrictService;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class DistrictRESTControllerImpl implements DistrictRESTController {

    private final DistrictService districtService;

    public List<ResponseDistrict> showDistricts() {
        return districtService.getAllDistrict();
    }

    public ResponseDistrict showDistrict(@PathVariable UUID id) {
        return districtService.getDistrictById(id);
    }

    public ResponseDistrict addDistrict (@RequestBody CreateRequestDistrict createRequest) {
        return districtService.saveNewDistrict(createRequest);
    }

    public ResponseDistrict updateDistrict (@PathVariable UUID id, @RequestBody UpdateRequestDistrict updateReques) {
        return districtService.updateDistrict(id, updateReques);
    }

    public void deleteDistrict(@PathVariable UUID id) {
        districtService.deleteDistrict(id);
    }
}