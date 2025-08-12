package sadupstaff.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseEmployee {

    private String personelNumber;

    private String firstName;

    private String lastName;

    private String patronymic;

    private String position;

    private String departmentName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}