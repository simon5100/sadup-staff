package sadupstaff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sadupstaff.entity.district.Section;

import java.util.List;
import java.util.UUID;

public interface SectionRepository extends JpaRepository<Section, UUID> {

    Section findSectionByName(String name);
}
