package sadupstaff.service.district;

import sadupstaff.dto.request.district.CreateDistrictRequest;
import sadupstaff.dto.request.district.UpdateDistrictRequest;
import sadupstaff.dto.response.DistrictResponse;
import sadupstaff.entity.district.District;
import java.util.List;
import java.util.UUID;

public interface DistrictService {

    List<DistrictResponse> getAllDistrict();

    DistrictResponse getDistrictById(UUID id);

    District getDistrictByIdForUpdate(UUID id);

    District getDistrictByName(String name);

    DistrictResponse saveDistrict(CreateDistrictRequest createRequest);

    DistrictResponse updateDistrict(UUID id, UpdateDistrictRequest updateRequest);

    void deleteDistrict(UUID id);
}
