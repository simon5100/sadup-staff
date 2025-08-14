package sadupstaff.service.district;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sadupstaff.dto.request.create.CreateDistrictRequest;
import sadupstaff.dto.request.update.UpdateDistrictRequest;
import sadupstaff.dto.response.DistrictResponse;
import sadupstaff.entity.district.District;
import sadupstaff.mapper.district.CreateDistrictMapper;
import sadupstaff.mapper.district.FindDistrictMapper;
import sadupstaff.mapper.district.UpdateDistrictMapper;
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
    private final UpdateDistrictMapper updateDistrictMapper;
    private final FindDistrictMapper findDistrictMapper;
    private final CreateDistrictMapper createDistrictMapper;

    @Override
    @Transactional
    public List<DistrictResponse> getAllDistrict() {
        return  districtRepository.findAll().stream()
                .map(district -> findDistrictMapper.entityToResponse(district))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DistrictResponse getDistrictById(UUID id) {
        Optional<District> districtOptional = districtRepository.findById(id);
        if (districtOptional.isPresent()) {
            return findDistrictMapper.entityToResponse(districtOptional.get());
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
        District district = createDistrictMapper.toEntity(createRequest);
        district.setCreatedAt(LocalDateTime.now());
        district.setUpdatedAt(LocalDateTime.now());
        district = districtRepository.save(district);

        return getDistrictById(district.getId());
    }

    @Override
    @Transactional
    public DistrictResponse updateDistrict(UUID id, UpdateDistrictRequest updateData) {
        District districtOld = getDistrictByIdForUpdate(id);
        updateDistrictMapper.updateDistrictData(updateData, districtOld);
        districtOld.setUpdatedAt(LocalDateTime.now());
        districtRepository.save(districtOld);

        return findDistrictMapper.entityToResponse(districtOld);
    }

    @Override
    @Transactional
    public void deleteDistrict(UUID id) {
        districtRepository.deleteById(id);
    }
}
