package sadupstaff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sadupstaff.entity.management.Employee;

import java.util.UUID;

public interface EmploeeyRepository extends JpaRepository<Employee, UUID> {
}
