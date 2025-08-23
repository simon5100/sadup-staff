package sadupstaff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sadupstaff.entity.district.SectionEmployee;
import sadupstaff.enums.PositionSectionEmployeeEnum;

import java.util.UUID;

public interface SectionEmployeeRepository extends JpaRepository<SectionEmployee, UUID> {
    boolean existsSectionEmployeeByPosition(PositionSectionEmployeeEnum position);
}
