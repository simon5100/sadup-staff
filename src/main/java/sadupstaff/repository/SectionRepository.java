package sadupstaff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sadupstaff.entity.district.Section;
import java.util.UUID;

public interface SectionRepository extends JpaRepository<Section, UUID> {
}
