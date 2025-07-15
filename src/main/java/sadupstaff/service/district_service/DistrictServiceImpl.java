package sadupstaff.service.district_service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sadupstaff.entity.district.District;
import sadupstaff.repository.DistrictRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DistrictServiceImpl implements DistrictService{

    private final DistrictRepository districtRepository;


    @Override
    @Transactional
    public List<District> getAllDistrict() {
        return  districtRepository.findAll();
    }

    @Override
    @Transactional
    public District getDistrict(UUID id) {
        District district = null;
        Optional<District> districtOptional = districtRepository.findById(id);
        if (districtOptional.isPresent()) {
            district = districtOptional.get();
        }
        return district;
    }

    @Override
    @Transactional
    public void saveDistrict(District district) {
        districtRepository.save(district);
    }

    @Override
    @Transactional
    public void deleteDistrict(UUID id) {
        districtRepository.deleteById(id);
    }
}
