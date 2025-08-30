package sadupstaff.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DepartmentNameEnum {
    CIVIL_SERVICE_AND_HR("Отдел государственной гражданской службы и кадров"),
    LEGAL_SUPPORT("Отдел правового обеспечения"),
    FINANCE_AND_PLANNING("Отдел финансирования и планирования"),
    LOGISTICS_SUPPORT("Отдел материально-технического обеспечения");

    private final String stringConvert;
}
