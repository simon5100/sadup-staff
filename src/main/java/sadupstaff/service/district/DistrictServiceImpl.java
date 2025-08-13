package sadupstaff.service.district;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sadupstaff.dto.district.UpdateDistrictDTO;
import sadupstaff.dto.request.district.CreateDistrictRequest;
import sadupstaff.dto.request.district.UpdateDistrictRequest;
import sadupstaff.dto.response.DistrictResponse;
import sadupstaff.entity.district.District;
import sadupstaff.mapper.district.MapperCreateDistrict;
import sadupstaff.mapper.district.MapperFindDistrict;
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
    private final MapperUpdateDistrict mapperUpdateDistrict;
    private final MapperFindDistrict mapperFindDistrict;
    private final MapperCreateDistrict mapperCreateDistrict;

    @Override
    @Transactional
    public List<DistrictResponse> getAllDistrict() {
        return  districtRepository.findAll().stream()
                .map(district -> mapperFindDistrict.entityToResponse(district))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DistrictResponse getDistrictById(UUID id) {
        Optional<District> districtOptional = districtRepository.findById(id);
        if (districtOptional.isPresent()) {
            return mapperFindDistrict.entityToResponse(districtOptional.get());
        }
        return null;
    }

    @Override
    public District getDistrictByIdForUpdate(UUID id) {
        Optional<District> districtOptional = districtRepository.findById(id);
        if (districtOptional.isPresent()) {
            return districtOptional.get();
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
    public DistrictResponse saveDistrict(CreateDistrictRequest createRequest) {
        District district = mapperCreateDistrict.toEntity(createRequest);
        district.setCreatedAt(LocalDateTime.now());
        district.setUpdatedAt(LocalDateTime.now());
        district = districtRepository.save(district);

        return getDistrictById(district.getId());
    }

    @Override
    @Transactional
    public DistrictResponse updateDistrict(UUID id, UpdateDistrictRequest updateRequest) {
        UpdateDistrictDTO updateData = mapperUpdateDistrict.updateRequestToDTO(updateRequest);
        UpdateDistrictDTO updateDistrictOld =
                mapperUpdateDistrict.entityToUpdateDistrictDTO(getDistrictByIdForUpdate(id));
        mapperUpdateDistrict.updateDistrictData(updateData, updateDistrictOld);
        District district = mapperUpdateDistrict.updateDTOToEntity(updateDistrictOld);
        district.setUpdatedAt(LocalDateTime.now());
        districtRepository.save(district);

        return getDistrictById(id);
    }

    @Override
    @Transactional
    public void deleteDistrict(UUID id) {
        districtRepository.deleteById(id);
    }
}
