package sadupstaff.sectionemployee;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import sadupstaff.dto.request.create.CreateSectionEmployeeRequest;
import sadupstaff.dto.request.update.UpdateSectionEmployeeRequest;
import sadupstaff.dto.response.SectionEmployeeResponse;
import sadupstaff.entity.district.SectionEmployee;
import sadupstaff.enums.PositionSectionEmployeeEnum;
import sadupstaff.exception.ErrorResponse;
import sadupstaff.mapper.sectionemployee.CreateSectionEmployeeMapper;
import sadupstaff.mapper.sectionemployee.FindSectionEmployeeMapper;
import sadupstaff.mapper.sectionemployee.UpdateSectionEmployeeMapper;
import sadupstaff.repository.SectionEmployeeRepository;
import sadupstaff.service.section.SectionServiceImpl;
import sadupstaff.service.sectionemployee.SectionEmployeeServiceImpl;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static sadupstaff.enums.PositionSectionEmployeeEnum.JUDGE;

@Log4j2
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
@DisplayName("E2E тесты методов SectionEmployeeServiceImpl")
public class SectionEmployeeServiceImplE2ETest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:17.5")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
        registry.add("spring.jpa.properties.hibernate.default_schema", () -> "sudstaff");
    }

    @MockitoSpyBean
    private UpdateSectionEmployeeMapper updateSectionEmployeeMapper;

    @MockitoSpyBean
    private SectionEmployeeRepository sectionEmployeeRepository;

    @MockitoSpyBean
    private SectionServiceImpl sectionService;

    @MockitoSpyBean
    private FindSectionEmployeeMapper findSectionEmployeeMapper;

    @MockitoSpyBean
    private CreateSectionEmployeeMapper createSectionEmployeeMapper;

    @MockitoSpyBean
    private SectionEmployeeServiceImpl sectionEmployeeService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestRestTemplate restTemplate;

    private static String URL = "/api/v1/sectionEmployees";

    private CreateSectionEmployeeRequest createRequest;

    private UpdateSectionEmployeeRequest updateRequest;

    private SectionEmployeeResponse response;

    private UUID id =  UUID.fromString("4d30f1c3-e70d-42a0-a3d3-58a5c2d50d04");

    private UUID badId = UUID.randomUUID();

    private ResponseEntity<SectionEmployeeResponse> responseEntity;

    private ResponseEntity<ErrorResponse> responseError;

    private void initializeSchema() {
        jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS sudstaff");
        log.info("Schema sudstaff created");
    }

    private void initializeTables() {

        jdbcTemplate.execute(
                "CREATE TABLE IF not EXISTS sudstaff.district (\n" +
                        "id UUID         PRIMARY KEY,\n" +
                        "name            VARCHAR(255)    NOT NULL,\n" +
                        "description     TEXT,\n" +
                        "created_at      TIMESTAMP       NOT NULL,\n" +
                        "updated_at      TIMESTAMP       NOT NULL,\n" +
                        "max_number_sections  INTEGER    NOT NULL   DEFAULT 2\n" +
                        ");"
        );

        jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS sudstaff.section (\n" +
                        "id UUID PRIMARY KEY,\n" +
                        "personel_number VARCHAR(255)    NOT NULL,\n" +
                        "name VARCHAR(255)               NOT NULL,\n" +
                        "created_at TIMESTAMP            NOT NULL,\n" +
                        "updated_at TIMESTAMP            NOT NULL,\n" +
                        "district_id UUID                NOT NULL,\n" +
                        "max_number_employees_section    INTEGER    NOT NULL   DEFAULT 3,\n" +
                        "CONSTRAINT fk_district\n" +
                        "FOREIGN KEY (district_id)\n" +
                        "   REFERENCES sudstaff.district(id)\n" +
                        "   ON DELETE CASCADE\n" +
                        ");"
        );

        jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS sudstaff.section_employee (\n" +
                        "id UUID PRIMARY KEY,\n" +
                        "personel_number VARCHAR(255) NOT NULL,\n" +
                        "first_name VARCHAR(255) NOT NULL,\n" +
                        "last_name VARCHAR(255) NOT NULL,\n" +
                        "patronymic VARCHAR(255),\n" +
                        "position VARCHAR(255),\n" +
                        "created_at TIMESTAMP,\n" +
                        "updated_at TIMESTAMP,\n" +
                        "section_id UUID,\n" +
                        "\n" +
                        "CONSTRAINT fk_section\n" +
                        "FOREIGN KEY (section_id)\n" +
                        "   REFERENCES sudstaff.section(id)\n" +
                        "   ON DELETE CASCADE\n" +
                        ");"
        );
    }

    private void initializeData() {
        jdbcTemplate.execute(
                "insert into sudstaff.district (id, name, description, created_at, updated_at, max_number_sections)\n" +
                        "values (\n" +
                        "'1d30f1c3-e70d-42a0-a3d3-58a5c2d50d04',\n" +
                        "'CENTRALNY',\n" +
                        "'Находится со мной в здании',\n" +
                        "'2025.07.30 15:17:00',\n" +
                        "'2025.07.30 15:17:00',\n" +
                        "2\n" +
                        ");"
        );

        jdbcTemplate.execute(
                "insert into sudstaff.section (id, personel_number, name, created_at, updated_at, district_id, max_number_employees_section)\n" +
                        "values (\n" +
                        "'3d30f1c3-e70d-42a0-a3d3-58a5c2d50d04',\n" +
                        "'M540000',\n" +
                        "'1й участок центрального района',\n" +
                        "'2025.07.30 15:17:00',\n" +
                        "'2025.07.30 15:17:00',\n" +
                        "'1d30f1c3-e70d-42a0-a3d3-58a5c2d50d04',\n" +
                        "3" +
                        ");"
        );

        jdbcTemplate.execute(
                "insert into sudstaff.section_employee (id, personel_number, first_name, last_name, patronymic, position, created_at, updated_at, section_id)\n" +
                        "values (\n" +
                        "'4d30f1c3-e70d-42a0-a3d3-58a5c2d50d04',\n" +
                        "'EMP12345',\n" +
                        "'Иван',\n" +
                        "'Иванов',\n" +
                        "'Иванович',\n" +
                        "'JUDGE',\n" +
                        "'2025.07.30 15:17:00',\n" +
                        "'2025.07.30 15:17:00',\n" +
                        "'3d30f1c3-e70d-42a0-a3d3-58a5c2d50d04'\n" +
                        ");"
        );
        log.info("Data created");
    }

    @BeforeEach
    void setUp() {

        initializeSchema();
        initializeTables();

        jdbcTemplate.execute("Delete from sudstaff.section_employee");
        jdbcTemplate.execute("Delete from sudstaff.section");
        jdbcTemplate.execute("Delete from sudstaff.district");


        log.info("очистка таблиц");

        responseEntity = null;
        responseError = null;

        initializeData();

        createRequest = new CreateSectionEmployeeRequest(
                "EMP12345",
                "Иван",
                "Иванов",
                "Иванович",
                JUDGE,
                "1й участок центрального района"
        );

        updateRequest = new UpdateSectionEmployeeRequest();
    }



    @Nested
    @DisplayName("Тесты на метод getAllSectionEmployees поиска всех сотрудников")
    class GetAllSectionEmployeeTests {

        @Test
        @Tag("E2E")
        @DisplayName("Тест с позитивным исходом")
        void getAllSectionEmployeesTest() {

            SectionEmployeeResponse[] employeeResponses;
            ResponseEntity<SectionEmployeeResponse[]> responseEntityArr = restTemplate.getForEntity(URL, SectionEmployeeResponse[].class);
            employeeResponses = responseEntityArr.getBody();

            assertNotNull(employeeResponses);
            assertTrue(responseEntityArr.getStatusCode().isSameCodeAs(HttpStatus.OK));
            assertEquals(1, employeeResponses.length);
            assertEquals(employeeResponses[0].getPosition(), JUDGE.getStringConvert());
            assertEquals(employeeResponses[0].getSectionName(), "1й участок центрального района");

            verify(sectionEmployeeRepository, times(1)).findAll();
            verify(findSectionEmployeeMapper, times(1)).entityToResponse(any(SectionEmployee.class));
        }
    }

    @Nested
    @DisplayName("Тесты на метод getSectionEmployee поиска сотрудника по id")
    class GetSectionEmployeeByIdTests {

        @Test
        @Tag("E2E")
        @DisplayName("Тест с позитивным исходом")
        void getSectionEmployeeByIdTest() {

            responseEntity = restTemplate.getForEntity(URL + "/" +  id, SectionEmployeeResponse.class);
            response = responseEntity.getBody();

            assertNotNull(response);
            assertTrue(responseEntity.getStatusCode().isSameCodeAs(HttpStatus.OK));
            assertEquals(response.getPosition(), JUDGE.getStringConvert());
            assertEquals(response.getSectionName(), "1й участок центрального района");

            verify(sectionEmployeeRepository, times(1)).findById(id);
            verify(findSectionEmployeeMapper, times(1)).entityToResponse(any(SectionEmployee.class));
        }

        @Test
        @Tag("E2E")
        @DisplayName("Тест с выбросом IdNotFoundException")
        void getSectionEmployeeByIdNotFoundIdTest() {

            responseError = restTemplate.getForEntity(URL + "/" +  badId, ErrorResponse.class);

            assertTrue(responseError.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND));
            assertEquals("Id '" + badId + "' не найден", responseError.getBody().getMessage());

            verify(sectionEmployeeRepository, times(1)).findById(badId);
            verify(findSectionEmployeeMapper, never()).entityToResponse(any(SectionEmployee.class));
        }
    }

    @Nested
    @DisplayName("Тесты на метод saveSectionEmployee сохранения сотрудника")
    class SaveSectionEmployeeTests {

        @ParameterizedTest
        @EnumSource(value = PositionSectionEmployeeEnum.class,
                mode = EnumSource.Mode.EXCLUDE,
                names = {"JUDGE"}
        )
        @Tag("E2E")
        @DisplayName("Тест с позитивным исходом")
        void saveSectionEmployeeTest(PositionSectionEmployeeEnum position) {

            createRequest.setPosition(position);

            responseEntity = restTemplate.postForEntity(URL, createRequest, SectionEmployeeResponse.class);
            response = responseEntity.getBody();

            assertTrue(responseEntity.getStatusCode().isSameCodeAs(HttpStatus.OK));
            assertNotNull(response);
            assertEquals(response.getPosition(), position.getStringConvert());

            verify(createSectionEmployeeMapper, times(1)).toEntity(any(CreateSectionEmployeeRequest.class));
            verify(sectionService, times(1)).getSectionByName(any(String.class));
            verify(sectionEmployeeRepository, times(1)).findById(any(UUID.class));
            verify(sectionEmployeeRepository, times(1)).save(any(SectionEmployee.class));
            verify(findSectionEmployeeMapper, times(1)).entityToResponse(any(SectionEmployee.class));
        }

        @ParameterizedTest
        @EnumSource(value = PositionSectionEmployeeEnum.class,
                mode = EnumSource.Mode.INCLUDE,
                names = {"JUDGE"}
        )
        @Tag("E2E")
        @DisplayName("Тест на выброс PositionOccupiedException")
        void saveSectionEmployeePositionOccupiedTest(PositionSectionEmployeeEnum position) {

            createRequest.setPosition(position);

            responseError = restTemplate.postForEntity(URL, createRequest, ErrorResponse.class);

            assertNotNull(responseError);
            assertTrue(responseError.getStatusCode().isSameCodeAs(HttpStatus.CONFLICT));
            assertEquals("Позиция '" + position.getStringConvert() + "' уже занята", responseError.getBody().getMessage());

            verify(createSectionEmployeeMapper, times(1)).toEntity(any(CreateSectionEmployeeRequest.class));
            verify(sectionService, times(1)).getSectionByName(any(String.class));
            verify(sectionEmployeeRepository, never()).findById(any(UUID.class));
            verify(sectionEmployeeRepository, never()).save(any(SectionEmployee.class));
            verify(findSectionEmployeeMapper, never()).entityToResponse(any(SectionEmployee.class));
        }

        @ParameterizedTest
        @EnumSource(PositionSectionEmployeeEnum.class)
        @Tag("E2E")
        @DisplayName("Тест на выброс MaxEmployeeInSectionException")
        void saveEmployeeMaxEmployeeInSectionTest(PositionSectionEmployeeEnum position) {

            jdbcTemplate.execute(
                    "insert into sudstaff.section_employee (id, personel_number, first_name, last_name, patronymic, position, created_at, updated_at, section_id)\n" +
                            "values (\n" +
                            "'5d30f1c3-e70d-42a0-a3d3-58a5c2d50d04',\n" +
                            "'EMP12345',\n" +
                            "'Иван',\n" +
                            "'Иванов',\n" +
                            "'Иванович',\n" +
                            "'JUDGE_ASSISTANT',\n" +
                            "'2025.07.30 15:17:00',\n" +
                            "'2025.07.30 15:17:00',\n" +
                            "'3d30f1c3-e70d-42a0-a3d3-58a5c2d50d04'\n" +
                            ");"
            );

            jdbcTemplate.execute(
                    "insert into sudstaff.section_employee (id, personel_number, first_name, last_name, patronymic, position, created_at, updated_at, section_id)\n" +
                            "values (\n" +
                            "'6d30f1c3-e70d-42a0-a3d3-58a5c2d50d04',\n" +
                            "'EMP12345',\n" +
                            "'Иван',\n" +
                            "'Иванов',\n" +
                            "'Иванович',\n" +
                            "'SECRETARY_SESSION',\n" +
                            "'2025.07.30 15:17:00',\n" +
                            "'2025.07.30 15:17:00',\n" +
                            "'3d30f1c3-e70d-42a0-a3d3-58a5c2d50d04'\n" +
                            ");"
            );

            createRequest.setPosition(position);

            responseError = restTemplate.postForEntity(URL, createRequest, ErrorResponse.class);

            assertNotNull(responseError);
            assertTrue(responseError.getStatusCode().isSameCodeAs(HttpStatus.CONFLICT));
            assertEquals("В '" + createRequest.getSectionName() + "' максимальное количество сотрудников", responseError.getBody().getMessage());

            verify(createSectionEmployeeMapper, times(1)).toEntity(any(CreateSectionEmployeeRequest.class));
            verify(sectionService, times(1)).getSectionByName(any(String.class));
            verify(sectionEmployeeRepository, never()).findById(any(UUID.class));
            verify(sectionEmployeeRepository, never()).save(any(SectionEmployee.class));
            verify(findSectionEmployeeMapper, never()).entityToResponse(any(SectionEmployee.class));
        }
    }

    @Nested
    @DisplayName("Тесты на метод updateSectionEmployee обновления данных сотрудника")
    class UpdateSectionEmployeeTests {

        @ParameterizedTest
        @EnumSource(value = PositionSectionEmployeeEnum.class,
                mode = EnumSource.Mode.EXCLUDE,
                names = {"JUDGE"}
        )
        @Tag("E2E")
        @DisplayName("Тест с позитивным исходом")
        void updateSectionEmployeeTest(PositionSectionEmployeeEnum position) {

            updateRequest.setPosition(position);

            responseEntity = restTemplate.exchange(
                    URL + "/" + id,
                    HttpMethod.PUT,
                    new HttpEntity<>(updateRequest),
                    SectionEmployeeResponse.class);

            response = responseEntity.getBody();

            assertNotNull(response);
            assertTrue(responseEntity.getStatusCode().isSameCodeAs(HttpStatus.OK));
            assertEquals(response.getPosition(), position.getStringConvert());

            verify(sectionEmployeeRepository, times(1)).findById(any(UUID.class));
            verify(sectionEmployeeRepository, times(1)).existsSectionEmployeeByPosition(position);
            verify(updateSectionEmployeeMapper, times(1)).updateSectionEmployeeData(any(UpdateSectionEmployeeRequest.class), any(SectionEmployee.class));
            verify(sectionEmployeeRepository, times(1)).save(any(SectionEmployee.class));
            verify(findSectionEmployeeMapper, times(1)).entityToResponse(any(SectionEmployee.class));
        }

        @Test
        @Tag("E2E")
        @DisplayName("Тест на выброс IdNotFoundException")
        void updateSectionEmployeeIdNotFoundTest() {

            responseError = restTemplate.exchange(
                    URL + "/" + badId,
                    HttpMethod.PUT,
                    new HttpEntity<>(updateRequest),
                    ErrorResponse.class);

            assertTrue(responseError.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND));
            assertEquals("Id '" + badId + "' не найден", responseError.getBody().getMessage());

            verify(sectionEmployeeRepository, times(1)).findById(badId);
            verify(sectionEmployeeRepository, never()).existsSectionEmployeeByPosition(any(PositionSectionEmployeeEnum.class));
            verify(updateSectionEmployeeMapper, never()).updateSectionEmployeeData(any(UpdateSectionEmployeeRequest.class), any(SectionEmployee.class));
            verify(sectionEmployeeRepository, never()).save(any(SectionEmployee.class));
            verify(findSectionEmployeeMapper, never()).entityToResponse(any(SectionEmployee.class));
        }

        @ParameterizedTest
        @EnumSource(value = PositionSectionEmployeeEnum.class,
                mode = EnumSource.Mode.INCLUDE,
                names = {"JUDGE"}
        )
        @Tag("E2E")
        @DisplayName("Тест на выброс PositionOccupiedException")
        void updateSectionEmployeePositionOccupiedTest(PositionSectionEmployeeEnum position) {

            updateRequest.setPosition(position);

            responseError = restTemplate.exchange(
                    URL + "/" + id,
                    HttpMethod.PUT,
                    new HttpEntity<>(updateRequest),
                    ErrorResponse.class);

            assertTrue(responseError.getStatusCode().isSameCodeAs(HttpStatus.CONFLICT));
            assertEquals("Позиция '" + position.getStringConvert() + "' уже занята", responseError.getBody().getMessage());

            verify(sectionEmployeeRepository, times(1)).findById(id);
            verify(sectionEmployeeRepository, times(1)).existsSectionEmployeeByPosition(any(PositionSectionEmployeeEnum.class));
            verify(updateSectionEmployeeMapper, never()).updateSectionEmployeeData(any(UpdateSectionEmployeeRequest.class), any(SectionEmployee.class));
            verify(sectionEmployeeRepository, never()).save(any(SectionEmployee.class));
            verify(findSectionEmployeeMapper, never()).entityToResponse(any(SectionEmployee.class));
        }
    }

    @Nested
    @DisplayName("Тесты на метод deleteSectionEmployee удаления сотрудника по id")
    class DeleteEmployeeTests {

        @Test
        @Tag("E2E")
        @DisplayName("Тест с позитивным исходом")
        void deleteSectionEmployeeByIdTest() {

            ResponseEntity<Void> status = restTemplate.exchange(
                    URL + "/" + id,
                    HttpMethod.DELETE,
                    new HttpEntity<>(Void.class),
                    Void.class
            );

            verify(sectionEmployeeRepository, times(1)).findById(id);
            verify(sectionEmployeeRepository, times(1)).deleteById(id);
        }

        @Test
        @Tag("E2E")
        @DisplayName("Тест на выброс IdNotFoundException")
        void deleteSectionEmployeeIdNotFoundTest() {

            ResponseEntity<ErrorResponse> status = restTemplate.exchange(
                    URL + "/" + badId,
                    HttpMethod.DELETE,
                    new HttpEntity<>(ErrorResponse.class),
                    ErrorResponse.class);

            assertTrue(status.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND));
            assertEquals("Id '" + badId + "' не найден", status.getBody().getMessage());

            verify(sectionEmployeeRepository, times(1)).findById(any(UUID.class));
            verify(sectionEmployeeRepository, never()).deleteById(any(UUID.class));
        }
    }
}