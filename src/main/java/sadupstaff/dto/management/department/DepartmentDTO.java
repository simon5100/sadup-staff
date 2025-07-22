package sadupstaff.dto.management.department;

import lombok.AllArgsConstructor;
import lombok.Data;
import sadupstaff.dto.management.employee.EmployeeDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class DepartmentDTO {

    final private UUID id;

    final private String name;

    final private String description;

    final private LocalDateTime createdAt;

    final private LocalDateTime updatedAt;

    final private List<EmployeeDTO> emps;
}
