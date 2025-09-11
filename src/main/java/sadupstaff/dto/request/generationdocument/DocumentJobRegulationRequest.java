package sadupstaff.dto.request.generationdocument;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentJobRegulationRequest {

    @NotNull
    String judgeName;

    @NotNull
    Boolean actingJudge = false;

    @NotNull
    String judgeOrganizerName;

    @NotNull
    Boolean actingJudgeOrganizer = false;

    @NotNull
    String concordantName;
}
