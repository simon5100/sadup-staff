package sadupstaff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sadupstaff.entity.management.Department;
import sadupstaff.enums.DepartmentName;

import java.util.UUID;

public interface DepartmentRepository extends JpaRepository<Department, UUID> {

    Department findDepartmentByName(DepartmentName name);

    Boolean existsDistinctByName(DepartmentName name);
}
