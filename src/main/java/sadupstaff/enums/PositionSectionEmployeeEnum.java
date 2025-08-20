package sadupstaff.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PositionSectionEmployeeEnum {
    JUDGE_ORGANIZER("Судья организатор"),
    JUDGE("Судья"),
    JUDGE_ASSISTANT("Помощник мирового судьи"),
    SECRETARY_SESSION("Секретарь судебного заседания"),
    SECRETARY_SECTION("Секретарь судебного участка"),
    SPECIALIST("Специалист");

    private final String stringConvert;
}
