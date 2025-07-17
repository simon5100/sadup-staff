package sadupstaff.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sadupstaff.entity.district.District;
import sadupstaff.service.district_service.DistrictService;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DistrictRESTController {

    private final DistrictService districtService;

    @GetMapping("/districts")
    public List<District> showDistricts() {
        return districtService.getAllDistrict();
    }

    @GetMapping("/districts/{id}")
    public District showDistrict(@PathVariable UUID id) {
        return districtService.getDistrict(id);
    }

    @PostMapping("/districts")
    public District addDistrict (@RequestBody District district) {
        districtService.saveDistrict(district);
        return district;
    }

    @PutMapping("/districts/{id}")
    public District updateDistrict (@PathVariable UUID id, @RequestBody District district) {
        districtService.updateDistrict(id, district);
        return districtService.getDistrict(id);
    }

    @DeleteMapping("/districts/{id}")
    public void deleteDistrict(@PathVariable UUID id) {
        districtService.deleteDistrict(id);
    }
}