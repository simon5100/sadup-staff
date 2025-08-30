package sadupstaff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sadupstaff.entity.management.Employee;
import sadupstaff.enums.PositionEmployeeEnum;

import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    boolean existsEmployeeByPosition(PositionEmployeeEnum position);
}
