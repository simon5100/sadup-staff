package sadupstaff.service.district;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sadupstaff.dto.district.DistrictDTO;
import sadupstaff.dto.district.UpdateDistrictDTO;
import sadupstaff.dto.request.district.CreateRequestDistrict;
import sadupstaff.dto.request.district.UpdateRequestDistrict;
import sadupstaff.dto.response.ResponseDistrict;
import sadupstaff.entity.district.District;
import sadupstaff.mapper.district.MapperCreateDistrict;
import sadupstaff.mapper.district.MapperDistrict;
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
    private final MapperDistrict mapperDistrict;
    private final MapperUpdateDistrict mapperUpdateDistrict;
    private final MapperFindDistrict mapperFindDistrict;
    private final MapperCreateDistrict mapperCreateDistrict;

    @Override
    @Transactional
    public List<ResponseDistrict> getAllDistrict() {
        return  districtRepository.findAll().stream()
                .map(district -> mapperFindDistrict.entityToResponseDistrict(district))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ResponseDistrict getDistrictById(UUID id) {
        Optional<District> districtOptional = districtRepository.findById(id);
        if (districtOptional.isPresent()) {
            return mapperFindDistrict.entityToResponseDistrict(districtOptional.get());
        }
        return null;
    }

    @Override
    public DistrictDTO getDistrictByIdForUpdate(UUID id) {
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
    public ResponseDistrict saveNewDistrict(CreateRequestDistrict createRequest) {
        District district = mapperCreateDistrict.toEntity(createRequest);
        district.setCreatedAt(LocalDateTime.now());
        district.setUpdatedAt(LocalDateTime.now());
        district = districtRepository.save(district);

        return getDistrictById(district.getId());
    }

    @Override
    @Transactional
    public ResponseDistrict updateDistrict(UUID id, UpdateRequestDistrict updateReques) {
        UpdateDistrictDTO updateData = mapperUpdateDistrict.updateRequestToDTO(updateReques);
        DistrictDTO districtDTO = getDistrictByIdForUpdate(id);
        UpdateDistrictDTO updateDistrictDTO = mapperUpdateDistrict.toDTO(districtDTO);
        mapperUpdateDistrict.updateDistrictDTO(updateData, updateDistrictDTO);
        districtDTO = mapperUpdateDistrict.updateDTOToDTO(updateDistrictDTO);
        District district = mapperDistrict.toDistrict(districtDTO);
        districtRepository.save(district);

        return getDistrictById(id);
    }

    @Override
    @Transactional
    public void deleteDistrict(UUID id) {
        districtRepository.deleteById(id);
    }
}
