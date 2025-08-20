package sadupstaff.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DistrictNameEnum {
    DZERZHINSKY("Дзержинский"),
    ZHELEZNODOROZHHNY("Железнодорожный"),
    ZAELTSOVSKY("Заельцовский"),
    KALININSKY("Калининский"),
    KIROVSKY("Кировский"),
    LENINSKY("Ленинский"),
    OCTYABRSKY("Октябрьский"),
    PERVOMAISKY("Первомайский"),
    SOVETSKY("Советский"),
    CENTRALNY("Центральный"),
    BARABINSKY("Барабинский"),
    BERDSK("г. Бердск"),
    BOLOTNINSKY("Болотнинский"),
    VENGEROVSKY("Венгеровский"),
    DOVOLENSKY("Доволенский"),
    ISKITIMSKY("Искитимский"),
    KARASUKSKY("Карасукский"),
    KOLYVANSKY("Колыванский"),
    KOCHENEVSKY("Коченевский"),
    KRASNOZERSKY("Краснозерский"),
    KUIBYSHEVSKY("Куйбышевский"),
    KUPINSKY("Купинский"),
    MOSHKOVSKY("Мошковский"),
    NOVOSIBIRSKY("Новосибирский"),
    OBI("г. Оби"),
    ORDYNSKY("Ордынский"),
    SUZUNSKY("Сузунский"),
    TATARSKY("Татарский"),
    TOGUCHINSKY("Тогучинский"),
    CHANOVSKY("Чановский"),
    CHEREPANOVSKY("Черепановский"),
    CHULYMSKY("Чулымский");

    private final String stringConvert;
}
