package sadupstaff.service.district;

import sadupstaff.dto.district.DistrictDTO;
import sadupstaff.dto.request.district.CreateRequestDistrict;
import sadupstaff.dto.request.district.UpdateRequestDistrict;
import sadupstaff.dto.response.ResponseDistrict;
import sadupstaff.entity.district.District;
import java.util.List;
import java.util.UUID;

public interface DistrictService {

    List<ResponseDistrict> getAllDistrict();

    ResponseDistrict getDistrictById(UUID id);

    DistrictDTO getDistrictByIdForUpdate(UUID id);

    District getDistrictByName(String name);

    ResponseDistrict saveNewDistrict(CreateRequestDistrict createRequest);

    ResponseDistrict updateDistrict(UUID id, UpdateRequestDistrict updateReques);

    void deleteDistrict(UUID id);
}
