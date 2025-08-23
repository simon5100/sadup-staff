package sadupstaff.service.district;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sadupstaff.dto.request.create.CreateDistrictRequest;
import sadupstaff.dto.request.update.UpdateDistrictRequest;
import sadupstaff.dto.response.DistrictResponse;
import sadupstaff.entity.district.District;
import sadupstaff.enums.DistrictNameEnum;
import sadupstaff.exception.DistrictNotFoundException;
import sadupstaff.exception.IdNotFoundException;
import sadupstaff.exception.PositionOccupiedException;
import sadupstaff.exception.district.DeleteDistrictException;
import sadupstaff.mapper.district.CreateDistrictMapper;
import sadupstaff.mapper.district.FindDistrictMapper;
import sadupstaff.mapper.district.UpdateDistrictMapper;
import sadupstaff.repository.DistrictRepository;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
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
        District district = districtRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(id.toString()));

        return findDistrictMapper.entityToResponse(district);

    }

    @Override
    public District getDistrictByName(DistrictNameEnum name) {
        return Arrays.stream(DistrictNameEnum.values())
                .filter(value -> value.equals(name))
                .findFirst()
                .map(value -> districtRepository.findDistrictByName(value))
                .orElseThrow(() -> new DistrictNotFoundException(name.getStringConvert()));
    }

    @Override
    @Transactional
    public DistrictResponse saveDistrict(CreateDistrictRequest createRequest) {
        if (districtRepository.existsDistinctByName(createRequest.getName())) {
            throw new PositionOccupiedException(createRequest.getName().getStringConvert());
        }

        District district = createDistrictMapper.toEntity(createRequest);
        district.setCreatedAt(LocalDateTime.now());
        district.setUpdatedAt(LocalDateTime.now());
        district = districtRepository.save(district);

        return getDistrictById(district.getId());
    }

    @Override
    @Transactional
    public DistrictResponse updateDistrict(UUID id, UpdateDistrictRequest updateData) {
        District districtOld = districtRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(id.toString()));

        if (updateData.getName() != null && districtRepository.existsDistinctByName(updateData.getName())) {
            throw new PositionOccupiedException(updateData.getName().getStringConvert());
        }

        updateDistrictMapper.updateDistrictData(updateData, districtOld);
        districtOld.setUpdatedAt(LocalDateTime.now());
        districtRepository.save(districtOld);

        return findDistrictMapper.entityToResponse(districtOld);
    }

    @Override
    @Transactional
    public void deleteDistrict(UUID id) {
        District district = districtRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(id.toString()));
        if (!district.getSections().isEmpty()) {
            throw new DeleteDistrictException(district.getName().getStringConvert());
        }

        districtRepository.deleteById(id);
    }
}
