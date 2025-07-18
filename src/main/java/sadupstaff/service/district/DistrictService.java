package sadupstaff.service.district;

import sadupstaff.entity.district.District;
import java.util.List;
import java.util.UUID;

public interface DistrictService {

    public List<District> getAllDistrict();

    public District getDistrict(UUID id);

    public void saveDistrict(District district);

    public void updateDistrict(UUID id, District districtNew);

    public void deleteDistrict(UUID id);
}
