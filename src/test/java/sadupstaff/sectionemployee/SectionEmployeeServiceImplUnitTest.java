package sadupstaff.sectionemployee;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sadupstaff.dto.request.create.CreateSectionEmployeeRequest;
import sadupstaff.dto.request.create.CreateSectionRequest;
import sadupstaff.dto.request.update.UpdateSectionEmployeeRequest;
import sadupstaff.dto.request.update.UpdateSectionRequest;
import sadupstaff.dto.response.SectionEmployeeResponse;
import sadupstaff.dto.response.SectionResponse;
import sadupstaff.entity.district.Section;
import sadupstaff.entity.district.SectionEmployee;
import sadupstaff.enums.DistrictNameEnum;
import sadupstaff.enums.PositionSectionEmployeeEnum;
import sadupstaff.mapper.sectionemployee.CreateSectionEmployeeMapper;
import sadupstaff.mapper.sectionemployee.FindSectionEmployeeMapper;
import sadupstaff.mapper.sectionemployee.UpdateSectionEmployeeMapper;
import sadupstaff.repository.SectionEmployeeRepository;
import sadupstaff.service.section.SectionServiceImpl;
import sadupstaff.service.sectionemployee.SectionEmployeeServiceImpl;

import java.time.LocalDateTime;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit тесты методов SectionEmployeeServiceImpl")
public class SectionEmployeeServiceImplUnitTest {

    @Mock
    private UpdateSectionEmployeeMapper updateSectionEmployeeMapper;

    @Mock
    private SectionEmployeeRepository sectionEmployeeRepository;

    @Mock
    private SectionServiceImpl sectionService;

    @Mock
    private FindSectionEmployeeMapper findSectionEmployeeMapper;

    @Mock
    private CreateSectionEmployeeMapper createSectionEmployeeMapper;

    @InjectMocks
    private SectionEmployeeServiceImpl sectionEmployeeService;

    private SectionEmployee sectionEmployee;
    private CreateSectionEmployeeRequest createRequest;
    private UpdateSectionEmployeeRequest updateRequest;
    private SectionEmployeeResponse response;
    private UUID id;
    private UUID badId;

    @BeforeEach
    void setUp() {

        sectionEmployee = new SectionEmployee(
                UUID.fromString("4d30f1c3-e70d-42a0-a3d3-58a5c2d50d04"),
                "EMP12345",
                "Иван",
                "Иванов",
                "Иванович",
                PositionSectionEmployeeEnum.JUDGE,
                LocalDateTime.of(2025,07,30, 15,17,00,000),
                LocalDateTime.of(2025,07,30, 15,17,00,000),
                new Section()
        );

        id = sectionEmployee.getId();
        badId = UUID.randomUUID();

        createRequest = new CreateSectionEmployeeRequest(
                "EMP12345",
                "Иван",
                "Иванов",
                "Иванович",
                PositionSectionEmployeeEnum.JUDGE,
                "1"
        );

        response = new SectionEmployeeResponse(
                "EMP12345",
                "Иван",
                "Иванов",
                "Иванович",
                PositionSectionEmployeeEnum.JUDGE.getStringConvert(),
                "1"
        );

        updateRequest = new UpdateSectionEmployeeRequest();
    }

    @Nested
    @DisplayName("Тесты на метод getAllSectionEmployees поиска всех сотрудников")
    class GetAllSectionEmployeeTests {

        @Test
        @Tag("unit")
        @DisplayName("Тест с позитивным исходом")
        void getAllSectionEmployeesTest() {

        }
    }

    @Nested
    @DisplayName("Тесты на метод getSectionEmployee поиска сотрудника по id")
    class GetSectionEmployeeByIdTests {

        @Test
        @Tag("unit")
        @DisplayName("Тест с позитивным исходом")
        void getSectionEmployeeByIdTest() {


        }

        @Test
        @Tag("unit")
        @DisplayName("Тест с выбросом IdNotFoundException")
        void getSectionEmployeeByIdNotFoundIdTest() {


        }
    }

    @Nested
    @DisplayName("Тесты на метод saveSectionEmployee сохранения сотрудника")
    class SaveSectionEmployeeTests {

        @ParameterizedTest
        @EnumSource(DistrictNameEnum.class)
        @Tag("unit")
        @DisplayName("Тест с позитивным исходом")
        void saveSectionEmployeeTest(DistrictNameEnum name) {


        }

        @ParameterizedTest
        @EnumSource(DistrictNameEnum.class)
        @Tag("unit")
        @DisplayName("Тест на выброс PositionOccupiedException")
        void saveSectionEmployeePositionOccupiedTest(DistrictNameEnum name) {


        }
    }

    @Nested
    @DisplayName("Тесты на метод updateSectionEmployee обновления данных сотрудника")
    class UpdateSectionEmployeeTests {

        @ParameterizedTest
        @EnumSource(DistrictNameEnum.class)
        @Tag("unit")
        @DisplayName("Тест с позитивным исходом")
        void updateSectionEmployeeTest(DistrictNameEnum name) {


        }

        @Test
        @Tag("unit")
        @DisplayName("Тест на выброс IdNotFoundException")
        void updateSectionEmployeeIdNotFoundTest() {



        }

        @ParameterizedTest
        @EnumSource(DistrictNameEnum.class)
        @Tag("unit")
        @DisplayName("Тест на выброс PositionOccupiedException")
        void updateSectionEmployeePositionOccupiedTest(DistrictNameEnum name) {


        }
    }

    @Nested
    @DisplayName("Тесты на метод deleteSectionEmployee удаления сотрудника по id")
    class DeleteEmployeeTests {

        @Test
        @Tag("unit")
        @DisplayName("Тест с позитивным исходом")
        void deleteSectionEmployeeByIdTest() {


        }

        @Test
        @Tag("unit")
        @DisplayName("Тест на выброс IdNotFoundException")
        void deleteSectionEmployeeIdNotFoundTest() {


        }
    }
}
