package sadupstaff.service.district;

import sadupstaff.dto.district.DistrictDTO;
import sadupstaff.dto.district.UpdateDistrictDTO;
import sadupstaff.entity.district.District;
import java.util.List;
import java.util.UUID;

public interface DistrictService {

    public List<DistrictDTO> getAllDistrict();

    public DistrictDTO getDistrictById(UUID id);

    public District getDistrictByName(String name);

    public UUID saveDistrict(DistrictDTO districtDTO);

    public void updateDistrict(UUID id, UpdateDistrictDTO updateData);

    public void deleteDistrict(UUID id);
}
