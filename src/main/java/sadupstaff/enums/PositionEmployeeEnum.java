package sadupstaff.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PositionEmployeeEnum {
    HEAD_OF_DEPARTMENT("Начальник управления"),
    DEPUTY_HEAD_OF_DEPARTMENT("Заместитель начальника управления"),
    DEPARTMENT_HEAD("Начальник отдела"),
    DEPUTY_DEPARTMENT_HEAD("Заместитель начальника отдела"),
    CHIEF_ACCOUNTANT("Главный бухгалтер"),
    CONSULTANT("Консультант"),
    SENIOR_SPECIALIST("Главный специалист"),
    LEAD_EXPERT("Ведущий эксперт"),
    ECONOMIST_1ST_CATEGORY("Экономист 1-й категории"),
    CHIEF_EXPERT("Главный эксперт"),
    SENIOR_ENGINEER("Старший инженер");

    private final String stringConvert;
}
