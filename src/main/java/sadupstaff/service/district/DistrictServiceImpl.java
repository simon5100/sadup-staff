package sadupstaff.service.district;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sadupstaff.entity.district.District;
import sadupstaff.repository.DistrictRepository;
import sadupstaff.service.UpdatingData;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DistrictServiceImpl implements DistrictService{

    private final DistrictRepository districtRepository;

    private final UpdatingData updatingData;

    @Override
    @Transactional
    public List<District> getAllDistrict() {
        return  districtRepository.findAll();
    }

    @Override
    @Transactional
    public District getDistrict(UUID id) {
        Optional<District> districtOptional = districtRepository.findById(id);
        if (districtOptional.isPresent()) {
            return districtOptional.get();
        }
        return null;
    }

    @Override
    @Transactional
    public void saveDistrict(District district) {
        districtRepository.save(district);
    }

    @Override
    @Transactional
    public void updateDistrict(UUID id, District districtUpdate) {
        District district = getDistrict(id);
        districtRepository.save(updatingData.updatingData(district, districtUpdate, District.class));
    }

    @Override
    @Transactional
    public void deleteDistrict(UUID id) {
        districtRepository.deleteById(id);
    }
}
