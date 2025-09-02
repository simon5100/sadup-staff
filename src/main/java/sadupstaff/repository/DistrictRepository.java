package sadupstaff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sadupstaff.entity.district.District;
import sadupstaff.enums.DistrictName;

import java.util.UUID;

public interface DistrictRepository extends JpaRepository<District, UUID> {

    District findDistrictByName(DistrictName name);

    boolean existsDistinctByName(DistrictName name);
}
