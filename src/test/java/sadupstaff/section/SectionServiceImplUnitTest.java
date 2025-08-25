package sadupstaff.section;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sadupstaff.dto.request.create.CreateEmployeeRequest;
import sadupstaff.dto.request.create.CreateSectionRequest;
import sadupstaff.dto.request.update.UpdateEmployeeRequest;
import sadupstaff.dto.request.update.UpdateSectionRequest;
import sadupstaff.dto.response.EmployeeResponse;
import sadupstaff.dto.response.SectionResponse;
import sadupstaff.entity.district.District;
import sadupstaff.entity.district.Section;
import sadupstaff.entity.management.Employee;
import sadupstaff.enums.DistrictNameEnum;
import sadupstaff.mapper.section.CreateSectionMapper;
import sadupstaff.mapper.section.FindSectionMapper;
import sadupstaff.mapper.section.UpdateSectionMapper;
import sadupstaff.repository.SectionRepository;
import sadupstaff.service.district.DistrictServiceImpl;
import sadupstaff.service.section.SectionServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit тесты методов SectionServiceImpl")
public class SectionServiceImplUnitTest {

    @Mock
    private UpdateSectionMapper updateSectionMapper;

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private DistrictServiceImpl districtService;

    @Mock
    private FindSectionMapper findSectionMapper;

    @Mock
    private CreateSectionMapper createSectionMapper;

    @InjectMocks
    private SectionServiceImpl sectionService ;

    private Section section;
    private CreateSectionRequest createRequest;
    private UpdateSectionRequest updateRequest;
    private SectionResponse response;
    private UUID id;
    private UUID badId;

    @BeforeEach
    void setUp() {

        section = new Section(
                UUID.fromString("3d30f1c3-e70d-42a0-a3d3-58a5c2d50d04"),
                "M540000",
                "1й участок центрального района",
                3,
                LocalDateTime.of(2025,07,30, 15,17,00,000),
                LocalDateTime.of(2025,07,30, 15,17,00,000),
                new District(),
                List.of()
        );

        id = section.getId();

        badId = UUID.randomUUID();

        createRequest = new CreateSectionRequest(
                "M540000",
                "1й участок центрального района",
                3,
                DistrictNameEnum.CENTRALNY
        );

        response = new SectionResponse(
                "M540000",
                "1й участок центрального района",
                DistrictNameEnum.CENTRALNY.getStringConvert(),
                List.of()
        );

        updateRequest = new UpdateSectionRequest();
    }

    @Nested
    @DisplayName("Тесты на метод getAllSection поиска всех районов")
    class GetAllSectionsTests {

        @Test
        @Tag("unit")
        @DisplayName("Тест с позитивным исходом")
        void getAllSectionsTest() {

        }
    }

    @Nested
    @DisplayName("Тесты на метод getDistrictById поиска района по id")
    class GetSectionByIdTests {

        @Test
        @Tag("unit")
        @DisplayName("Тест с позитивным исходом")
        void getSectionByIdTest() {


        }

        @Test
        @Tag("unit")
        @DisplayName("Тест с выбросом IdNotFoundException")
        void getSectionByIdNotFoundIdTest() {


        }
    }

    @Nested
    @DisplayName("Тесты на метод getDistrictByName поиска района по имени")
    class GetSectionByNameTests {

        @ParameterizedTest
        @EnumSource(DistrictNameEnum.class)
        @Tag("unit")
        @DisplayName("Тест с позитивным исходом")
        void getSectionByNameTest(DistrictNameEnum name) {


        }
    }

    @Nested
    @DisplayName("Тесты на метод saveSection сохранения района")
    class SaveSectionTests {

        @ParameterizedTest
        @EnumSource(DistrictNameEnum.class)
        @Tag("unit")
        @DisplayName("Тест с позитивным исходом")
        void saveSectionTest(DistrictNameEnum name) {


        }

        @ParameterizedTest
        @EnumSource(DistrictNameEnum.class)
        @Tag("unit")
        @DisplayName("Тест на выброс PositionOccupiedException")
        void saveSectionPositionOccupiedTest(DistrictNameEnum name) {


        }
    }

    @Nested
    @DisplayName("Тесты на метод updateSection обновления данных района")
    class UpdateSectionTests {

        @ParameterizedTest
        @EnumSource(DistrictNameEnum.class)
        @Tag("unit")
        @DisplayName("Тест с позитивным исходом")
        void updateSectionTest(DistrictNameEnum name) {


        }

        @Test
        @Tag("unit")
        @DisplayName("Тест на выброс IdNotFoundException")
        void updateSectionIdNotFoundTest() {



        }

        @ParameterizedTest
        @EnumSource(DistrictNameEnum.class)
        @Tag("unit")
        @DisplayName("Тест на выброс PositionOccupiedException")
        void updateSectionPositionOccupiedTest(DistrictNameEnum name) {


        }
    }

    @Nested
    @DisplayName("Тесты на метод deleteSection удаления района по id")
    class DeleteSectionByIdTests {

        @Test
        @Tag("unit")
        @DisplayName("Тест с позитивным исходом")
        void deleteSectionByIdTest() {


        }

        @Test
        @Tag("unit")
        @DisplayName("Тест на выброс IdNotFoundException")
        void deleteSectionIdNotFoundTest() {


        }

        @Test
        @Tag("unit")
        @DisplayName("Тест на выброс DeleteSectionException")
        void deleteSectionByIdDeleteSectionExceptionTest() {


        }
    }
}
