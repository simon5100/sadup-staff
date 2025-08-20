package sadupstaff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sadupstaff.entity.management.Department;
import sadupstaff.enums.DepartmentNameEnum;

import java.util.UUID;

public interface DepartmentRepository extends JpaRepository<Department, UUID> {

    Department findDepartmentByName(DepartmentNameEnum name);

    Boolean existsDistinctByIdAndName(UUID id, DepartmentNameEnum name);

    Boolean existsDistinctByName(DepartmentNameEnum name);
}
