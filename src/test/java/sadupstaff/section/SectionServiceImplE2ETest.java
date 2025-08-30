package sadupstaff.section;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
import sadupstaff.dto.request.create.CreateSectionRequest;
import sadupstaff.dto.request.update.UpdateSectionRequest;
import sadupstaff.dto.response.SectionResponse;
import sadupstaff.entity.district.Section;
import sadupstaff.enums.DistrictNameEnum;
import sadupstaff.exception.ErrorResponse;
import sadupstaff.mapper.section.CreateSectionMapper;
import sadupstaff.mapper.section.FindSectionMapper;
import sadupstaff.mapper.section.UpdateSectionMapper;
import sadupstaff.repository.SectionRepository;
import sadupstaff.service.district.DistrictServiceImpl;
import sadupstaff.service.section.SectionServiceImpl;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Log4j2
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
@DisplayName("E2E тесты методов SectionServiceImpl")
public class SectionServiceImplE2ETest {

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
    private UpdateSectionMapper updateSectionMapper;

    @MockitoSpyBean
    private SectionRepository sectionRepository;

    @MockitoSpyBean
    private DistrictServiceImpl districtService;

    @MockitoSpyBean
    private FindSectionMapper findSectionMapper;

    @MockitoSpyBean
    private CreateSectionMapper createSectionMapper;

    @MockitoSpyBean
    private SectionServiceImpl sectionService ;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestRestTemplate restTemplate;

    private static String URL = "/api/v1/sections";

    private CreateSectionRequest createRequest;

    private UpdateSectionRequest updateRequest;

    private SectionResponse sectionResponse;

    private UUID id =  UUID.fromString("3d30f1c3-e70d-42a0-a3d3-58a5c2d50d04");

    private UUID badId = UUID.randomUUID();

    private ResponseEntity<SectionResponse> responseEntity;

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
        log.info("Data created");
    }

    @BeforeEach
    void setUp() {

        initializeSchema();
        initializeTables();

        jdbcTemplate.execute("Delete from sudstaff.section");
        jdbcTemplate.execute("Delete from sudstaff.district");
        log.info("очистка таблиц");

        responseEntity = null;
        responseError = null;
        initializeData();

        createRequest = new CreateSectionRequest(
                "M540001",
                "1",
                3,
                DistrictNameEnum.CENTRALNY
        );

        updateRequest = new UpdateSectionRequest();
    }

    @Nested
    @DisplayName("Тесты на метод getAllSection поиска всех районов")
    class GetAllSectionsTests {

        @Test
        @Tag("E2E")
        @DisplayName("Тест с позитивным исходом")
        void getAllSectionsTest() {

            SectionResponse[] sectionResponses;
            ResponseEntity<SectionResponse[]> responseEntity = restTemplate.getForEntity(URL, SectionResponse[].class);
            sectionResponses = responseEntity.getBody();

            assertNotNull(sectionResponses);
            assertTrue(responseEntity.getStatusCode().isSameCodeAs(HttpStatus.OK));
            assertEquals(1, sectionResponses.length);
            assertTrue(sectionResponses[0].getEmpsSect().isEmpty());
            assertEquals(sectionResponses[0].getName(), "1й участок центрального района");

            verify(sectionRepository).findAll();
            verify(findSectionMapper, times(1)).entityToResponse(any(Section.class));
        }
    }

    @Nested
    @DisplayName("Тесты на метод getDistrictById поиска района по id")
    class GetSectionByIdTests {

        @Test
        @Tag("E2E")
        @DisplayName("Тест с позитивным исходом")
        void getSectionByIdTest() {

            responseEntity = restTemplate.getForEntity(URL + "/" +  id, SectionResponse.class);
            sectionResponse = responseEntity.getBody();

            assertNotNull(sectionResponse);
            assertTrue(responseEntity.getStatusCode().isSameCodeAs(HttpStatus.OK));
            assertTrue(sectionResponse.getEmpsSect().isEmpty());
            assertEquals(sectionResponse.getName(), "1й участок центрального района");

            verify(sectionRepository).findById(id);
            verify(findSectionMapper, times(1)).entityToResponse(any(Section.class));
        }

        @Test
        @Tag("E2E")
        @DisplayName("Тест с выбросом IdNotFoundException")
        void getSectionByIdNotFoundIdTest() {

            responseError = restTemplate.getForEntity(URL + "/" +  badId, ErrorResponse.class);

            assertTrue(responseError.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND));
            assertEquals("Id '" + badId + "' не найден", responseError.getBody().getMessage());

            verify(sectionRepository).findById(badId);
            verify(findSectionMapper, never()).entityToResponse(any(Section.class));
        }
    }

    @Nested
    @DisplayName("Тесты на метод saveSection сохранения района")
    class SaveSectionTests {

        @Test
        @Tag("E2E")
        @DisplayName("Тест с позитивным исходом")
        void saveSectionTest() {

            responseEntity = restTemplate.postForEntity(URL, createRequest, SectionResponse.class);
            sectionResponse = responseEntity.getBody();

            assertNotNull(sectionResponse);
            assertTrue(responseEntity.getStatusCode().isSameCodeAs(HttpStatus.OK));
            assertTrue(sectionResponse.getEmpsSect().isEmpty());
            assertEquals(sectionResponse.getName(), "1");

            verify(createSectionMapper,times(1)).toEntity(any(CreateSectionRequest.class));
            verify(districtService,times(1)).getDistrictByName(any(DistrictNameEnum.class));
            verify(sectionRepository,times(1)).save(any(Section.class));
            verify(sectionRepository,times(1)).findById(any(UUID.class));
            verify(findSectionMapper,times(1)).entityToResponse(any(Section.class));
        }

        @Test
        @Tag("E2E")
        @DisplayName("Тест на выброс MaxSectionInDistrictException")
        void saveMaxSectionInDistrictTest() {

            jdbcTemplate.execute(
                    "insert into sudstaff.section (id, personel_number, name, created_at, updated_at, district_id, max_number_employees_section)\n" +
                            "values (\n" +
                            "'4d30f1c3-e70d-42a0-a3d3-58a5c2d50d04',\n" +
                            "'M540000',\n" +
                            "'2',\n" +
                            "'2025.07.30 15:17:00',\n" +
                            "'2025.07.30 15:17:00',\n" +
                            "'1d30f1c3-e70d-42a0-a3d3-58a5c2d50d04',\n" +
                            "3" +
                            ");"
            );

            responseError = restTemplate.postForEntity(URL, createRequest, ErrorResponse.class);

            assertNotNull(responseError);
            assertTrue(responseError.getStatusCode().isSameCodeAs(HttpStatus.CONFLICT));
            assertEquals("В '" + createRequest.getDistrictName().getStringConvert() + "' максимальное количество участков", responseError.getBody().getMessage());

            verify(createSectionMapper,times(1)).toEntity(any(CreateSectionRequest.class));
            verify(districtService,times(1)).getDistrictByName(any(DistrictNameEnum.class));
            verify(sectionRepository, never()).save(any(Section.class));
            verify(sectionRepository,never()).findById(any(UUID.class));
            verify(findSectionMapper,never()).entityToResponse(any(Section.class));
        }

        @Test
        @Tag("E2E")
        @DisplayName("Тест на выброс PositionOccupiedException")
        void saveSectionPositionOccupiedTest() {

            createRequest.setName("1й участок центрального района");

            responseError = restTemplate.postForEntity(URL, createRequest, ErrorResponse.class);

            assertNotNull(responseError);
            assertTrue(responseError.getStatusCode().isSameCodeAs(HttpStatus.CONFLICT));
            assertEquals("Позиция '" + createRequest.getName() + "' уже занята", responseError.getBody().getMessage());

            verify(createSectionMapper,times(1)).toEntity(any(CreateSectionRequest.class));
            verify(districtService,times(1)).getDistrictByName(any(DistrictNameEnum.class));
            verify(sectionRepository, never()).save(any(Section.class));
            verify(sectionRepository,never()).findById(any(UUID.class));
            verify(findSectionMapper,never()).entityToResponse(any(Section.class));
        }
    }

    @Nested
    @DisplayName("Тесты на метод updateSection обновления данных района")
    class UpdateSectionTests {

        @ParameterizedTest
        @ValueSource(strings = {"2", "3"})
        @Tag("E2E")
        @DisplayName("Тест с позитивным исходом")
        void updateSectionTest(String name) {

            updateRequest.setName(name);

            responseEntity = restTemplate.exchange(
                    URL + "/" + id,
                    HttpMethod.PUT,
                    new HttpEntity<>(updateRequest),
                    SectionResponse.class);

            sectionResponse = responseEntity.getBody();

            assertNotNull(sectionResponse);
            assertTrue(responseEntity.getStatusCode().isSameCodeAs(HttpStatus.OK));
            assertEquals(sectionResponse.getName(), name);

            verify(sectionRepository, times(1)).findById(any(UUID.class));
            verify(sectionRepository, times(1)).existsSectionByName(any(String.class));
            verify(updateSectionMapper, times(1)).update(any(UpdateSectionRequest.class), any(Section.class));
            verify(sectionRepository, times(1)).save(any(Section.class));
            verify(findSectionMapper, times(1)).entityToResponse(any(Section.class));
        }

        @Test
        @Tag("E2E")
        @DisplayName("Тест на выброс IdNotFoundException")
        void updateSectionIdNotFoundTest() {

            responseError = restTemplate.exchange(
                    URL + "/" + badId,
                    HttpMethod.PUT,
                    new HttpEntity<>(updateRequest),
                    ErrorResponse.class);

            assertTrue(responseError.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND));
            assertEquals("Id '" + badId + "' не найден", responseError.getBody().getMessage());

            verify(sectionRepository, times(1)).findById(badId);
            verify(sectionRepository, never()).existsSectionByName(any(String.class));
            verify(updateSectionMapper, never()).update(any(UpdateSectionRequest.class), any(Section.class));
            verify(sectionRepository, never()).save(any(Section.class));
            verify(findSectionMapper, never()).entityToResponse(any(Section.class));
        }

        @ParameterizedTest
        @ValueSource(strings = {"1й участок центрального района"})
        @Tag("E2E")
        @DisplayName("Тест на выброс PositionOccupiedException")
        void updateSectionPositionOccupiedTest(String name) {

            updateRequest.setName(name);

            responseError = restTemplate.exchange(
                    URL + "/" + id,
                    HttpMethod.PUT,
                    new HttpEntity<>(updateRequest),
                    ErrorResponse.class);

            assertTrue(responseError.getStatusCode().isSameCodeAs(HttpStatus.CONFLICT));
            assertEquals("Позиция '" + name + "' уже занята", responseError.getBody().getMessage());

            verify(sectionRepository, times(1)).findById(id);
            verify(sectionRepository, times(1)).existsSectionByName(any(String.class));
            verify(updateSectionMapper, never()).update(any(UpdateSectionRequest.class), any(Section.class));
            verify(sectionRepository, never()).save(any(Section.class));
            verify(findSectionMapper, never()).entityToResponse(any(Section.class));
        }
    }

    @Nested
    @DisplayName("Тесты на метод deleteSection удаления района по id")
    class DeleteSectionByIdTests {

        @Test
        @Tag("E2E")
        @DisplayName("Тест с позитивным исходом")
        void deleteSectionByIdTest() {

            ResponseEntity<Void> status = restTemplate.exchange(
                    URL + "/" + id,
                    HttpMethod.DELETE,
                    new HttpEntity<>(Void.class),
                    Void.class
            );

            verify(sectionRepository, times(1)).findById(any(UUID.class));
        }

        @Test
        @Tag("E2E")
        @DisplayName("Тест на выброс IdNotFoundException")
        void deleteSectionIdNotFoundTest() {

            ResponseEntity<ErrorResponse> status = restTemplate.exchange(
                    URL + "/" + badId,
                    HttpMethod.DELETE,
                    new HttpEntity<>(ErrorResponse.class),
                    ErrorResponse.class);

            assertTrue(status.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND));
            assertEquals("Id '" + badId + "' не найден", status.getBody().getMessage());

            verify(sectionRepository, times(1)).findById(badId);
        }

        @Test
        @Tag("E2E")
        @DisplayName("Тест на выброс DeleteSectionException")
        void deleteSectionByIdDeleteSectionExceptionTest() {

            jdbcTemplate.execute(
                    "insert into sudstaff.section_employee (id, personel_number, first_name, last_name, patronymic, position, created_at, updated_at, section_id)\n" +
                            "            values (\n" +
                            "                '4d30f1c3-e70d-42a0-a3d3-58a5c2d50d04',\n" +
                            "                'EMP12345',\n" +
                            "                'Иван',\n" +
                            "                'Иванов',\n" +
                            "                'Иванович',\n" +
                            "                'JUDGE',\n" +
                            "                '2025.07.30 15:17:00',\n" +
                            "                '2025.07.30 15:17:00',\n" +
                            "                '3d30f1c3-e70d-42a0-a3d3-58a5c2d50d04'\n" +
                            "                    );");

            ResponseEntity<ErrorResponse> status = restTemplate.exchange(
                    URL + "/" + id,
                    HttpMethod.DELETE,
                    new HttpEntity<>(ErrorResponse.class),
                    ErrorResponse.class);

            assertTrue(status.getStatusCode().isSameCodeAs(HttpStatus.UNPROCESSABLE_ENTITY));

            assertEquals("1й участок центрального района имеет сотрудников, удаление запрещено", status.getBody().getMessage());

            verify(sectionRepository, times(1)).findById(id);
        }
    }
}