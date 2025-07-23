package sadupstaff.dto.management.department;

import lombok.AllArgsConstructor;
import lombok.Data;
import sadupstaff.dto.management.employee.EmployeeDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UpdateDepartmentDTO {

    private UUID id;

    private String name;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<EmployeeDTO> emps;
}
