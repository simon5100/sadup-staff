package sadupstaff.service.district;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sadupstaff.dto.district.DistrictDTO;
import sadupstaff.dto.district.UpdateDistrictDTO;
import sadupstaff.entity.district.District;
import sadupstaff.mapper.district.MapperDistrict;
import sadupstaff.mapper.district.MapperUpdateDistrict;
import sadupstaff.repository.DistrictRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DistrictServiceImpl implements DistrictService{

    private final DistrictRepository districtRepository;
    private final MapperDistrict mapperDistrict;
    private final MapperUpdateDistrict mapperUpdateDistrict;

    @Override
    @Transactional
    public List<DistrictDTO> getAllDistrict() {

        return  districtRepository.findAll().stream()
                .map(district -> mapperDistrict.toDTO(district))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DistrictDTO getDistrictById(UUID id) {
        Optional<District> districtOptional = districtRepository.findById(id);
        if (districtOptional.isPresent()) {
            return mapperDistrict.toDTO(districtOptional.get());
        }
        return null;
    }

    @Override
    public District getDistrictByName(String name) {
        Optional<District> districtOptional = Optional.ofNullable(districtRepository.findDistrictByName(name));
        if (districtOptional.isPresent()) {
            return districtOptional.get();
        }
        return null;
    }

    @Override
    @Transactional
    public UUID saveDistrict(DistrictDTO districtDTO) {
        District district = mapperDistrict.toDistrict(districtDTO);
        if (district.getCreatedAt() == null) {
            district.setCreatedAt(LocalDateTime.now());
        }
        district.setUpdatedAt(LocalDateTime.now());
        return districtRepository.save(district).getId();
    }

    @Override
    @Transactional
    public void updateDistrict(UUID id, UpdateDistrictDTO updateData) {
        DistrictDTO districtDTO = getDistrictById(id);
        UpdateDistrictDTO updateDistrictDTO = mapperUpdateDistrict.toDTO(districtDTO);
        mapperUpdateDistrict.updateDistrictDTO(updateData, updateDistrictDTO);
        districtDTO = mapperUpdateDistrict.updateDTOToDTO(updateDistrictDTO);
        saveDistrict(districtDTO);
    }

    @Override
    @Transactional
    public void deleteDistrict(UUID id) {
        districtRepository.deleteById(id);
    }
}
