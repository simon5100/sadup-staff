package sadupstaff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sadupstaff.entity.district.SectionEmployee;
import java.util.UUID;

public interface SectionEmployeeRepository extends JpaRepository<SectionEmployee, UUID> {
}
