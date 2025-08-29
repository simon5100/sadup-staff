package sadupstaff.district;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
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
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import sadupstaff.dto.request.create.CreateDistrictRequest;
import sadupstaff.dto.request.update.UpdateDistrictRequest;
import sadupstaff.dto.response.DistrictResponse;
import sadupstaff.entity.district.District;
import sadupstaff.entity.district.Section;
import sadupstaff.enums.DistrictNameEnum;
import sadupstaff.exception.ErrorResponse;
import sadupstaff.exception.IdNotFoundException;
import sadupstaff.exception.PositionOccupiedException;
import sadupstaff.exception.district.DeleteDistrictException;
import sadupstaff.mapper.district.CreateDistrictMapper;
import sadupstaff.mapper.district.FindDistrictMapper;
import sadupstaff.mapper.district.UpdateDistrictMapper;
import sadupstaff.repository.DistrictRepository;
import sadupstaff.service.district.DistrictServiceImpl;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;
import static sadupstaff.enums.DistrictNameEnum.CENTRALNY;

@Log4j2
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
@DisplayName("E2E тесты методов DistrictServiceImpl")
public class DistrictServiceImplE2ETest {

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
    private DistrictRepository districtRepository;

    @MockitoSpyBean
    private UpdateDistrictMapper updateDistrictMapper;

    @MockitoSpyBean
    private FindDistrictMapper findDistrictMapper;

    @MockitoSpyBean
    private CreateDistrictMapper createDistrictMapper;

    @MockitoSpyBean
    private DistrictServiceImpl districtService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestRestTemplate restTemplate;

    private static String URL = "/api/v1/districts";

    private District district;

    private CreateDistrictRequest createRequest;

    private DistrictResponse districtResponse;

    private UpdateDistrictRequest updateRequest;

    private UUID id1 = UUID.fromString("1d30f1c3-e70d-42a0-a3d3-58a5c2d50d04");

    private UUID id2;

    private UUID badId = UUID.randomUUID();

    private ResponseEntity<DistrictResponse> responseEntity;

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

        log.info("tables created");
    }

    private void initializeDistricts() {
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

        log.info("districts created");
    }

    @BeforeEach
    void setUp() {
        initializeSchema();
        initializeTables();

        jdbcTemplate.execute("Delete from sudstaff.section");
        jdbcTemplate.execute("Delete from sudstaff.district");
        log.info("очистка таблиц района и участка");

        responseEntity = null;
        responseError = null;
        initializeDistricts();

        createRequest = new CreateDistrictRequest(
                CENTRALNY,
                2,
                "Находитсясо мной в здании");

        updateRequest = new UpdateDistrictRequest();

    }

    @Nested
    @DisplayName("Тесты на метод getDistrictById поиска района по id")
    class GetAllDistrictsTests {

        @Test
        @Tag("E2E")
        @DisplayName("Тест с позитивным исходом")
        void getAllDistrictsTest() {

            DistrictResponse[] districtResponses;
            ResponseEntity<DistrictResponse[]> responseEntity = restTemplate.getForEntity(URL, DistrictResponse[].class);
            districtResponses = responseEntity.getBody();

            assertNotNull(districtResponses);
            assertTrue(responseEntity.getStatusCode().isSameCodeAs(HttpStatus.OK));
            assertEquals(1, districtResponses.length);
            assertTrue(districtResponses[0].getSections().isEmpty());

            verify(districtService).getAllDistrict();
            verify(districtRepository).findAll();
            verify(findDistrictMapper, times(1)).entityToResponse(any(District.class));
        }
    }

    @Nested
    @DisplayName("Тесты на метод getDistrictById поиска района по id")
    class GetDistrictByIdTests {

        @Test
        @Tag("E2E")
        @DisplayName("Тест с позитивным исходом")
        void getDistrictByIdTest() {

            responseEntity = restTemplate.getForEntity(URL + "/" +  id1, DistrictResponse.class);

            districtResponse = responseEntity.getBody();

            assertNotNull(districtResponse);
            assertTrue(responseEntity.getStatusCode().isSameCodeAs(HttpStatus.OK));
            assertTrue(districtResponse.getSections().isEmpty());
            assertEquals(districtResponse.getName(), CENTRALNY.getStringConvert());

            verify(districtRepository, times(1)).findById(any(UUID.class));
            verify(findDistrictMapper, times(1)).entityToResponse(any(District.class));
        }

        @Test
        @Tag("E2E")
        @DisplayName("Тест с выбросом IdNotFoundException")
        void getDistrictByIdNotFoundIdTest() {

            responseError = restTemplate.getForEntity(URL + "/" +  badId, ErrorResponse.class);

            assertTrue(responseError.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND));


            assertEquals("Id '" + badId + "' не найден", responseError.getBody().getMessage());

            verify(districtRepository, times(1)).findById(any(UUID.class));
            verify(findDistrictMapper, never()).entityToResponse(any(District.class));
        }
    }

    @Nested
    @DisplayName("Тесты на метод saveDistrict сохранения района")
    class SaveDistrictTests {

        @ParameterizedTest
        @EnumSource(
                value = DistrictNameEnum.class,
                mode = EnumSource.Mode.EXCLUDE,
                names = {"CENTRALNY", "ZHELEZNODOROZHHNY", "ZAELTSOVSKY"}
        )
        @Tag("E2E")
        @DisplayName("Тест с позитивным исходом")
        void saveDistrictTest(DistrictNameEnum name) {

            createRequest.setName(name);
            createRequest.setDescription(name.getStringConvert());

            responseEntity = restTemplate.postForEntity(URL, createRequest, DistrictResponse.class);
            districtResponse = responseEntity.getBody();

            assertNotNull(districtResponse);
            assertTrue(responseEntity.getStatusCode().isSameCodeAs(HttpStatus.OK));
            assertEquals(name.getStringConvert(), districtResponse.getName());

            verify(districtRepository, times(1)).existsDistinctByName(any(DistrictNameEnum.class));
            verify(createDistrictMapper, times(1)).toEntity(any(CreateDistrictRequest.class));
            verify(districtRepository, times(1)).existsDistinctByName(any(DistrictNameEnum.class));
            verify(districtRepository, times(1)).save(any(District.class));
            verify(districtRepository, times(1)).findById(any(UUID.class));
            verify(findDistrictMapper, times(1)).entityToResponse(any(District.class));
        }

        @ParameterizedTest
        @EnumSource(
                value = DistrictNameEnum.class,
                mode = EnumSource.Mode.INCLUDE,
                names = {"CENTRALNY"}
        )
        @Tag("E2E")
        @DisplayName("Тест на выброс PositionOccupiedException")
        void saveDistrictPositionOccupiedTest(DistrictNameEnum name) {

            createRequest.setName(name);
            responseError = restTemplate.postForEntity(URL, createRequest, ErrorResponse.class);

            assertNotNull(responseError);
            assertTrue(responseError.getStatusCode().isSameCodeAs(HttpStatus.CONFLICT));
            assertEquals("Позиция '" + name.getStringConvert() + "' уже занята", responseError.getBody().getMessage());

            verify(districtRepository, times(1)).existsDistinctByName(any(DistrictNameEnum.class));
            verify(createDistrictMapper, never()).toEntity(any(CreateDistrictRequest.class));
            verify(districtRepository, never()).save(any(District.class));
            verify(districtRepository, never()).findById(any(UUID.class));
            verify(findDistrictMapper, never()).entityToResponse(any(District.class));
        }
    }

    @Nested
    @DisplayName("Тесты на метод updateDistrict обновления данных района")
    class UpdateDistrictTests {

        @ParameterizedTest
        @EnumSource(
                value = DistrictNameEnum.class,
                mode = EnumSource.Mode.EXCLUDE,
                names = {"CENTRALNY", "ZHELEZNODOROZHHNY", "ZAELTSOVSKY"}
        )
        @Tag("E2E")
        @DisplayName("Тест с позитивным исходом")
        void updateDistrictTest(DistrictNameEnum name) {

            updateRequest.setName(name);
            updateRequest.setDescription(name.getStringConvert());

            responseEntity = restTemplate.exchange(
                    URL + "/" + id1,
                    HttpMethod.PUT,
                    new HttpEntity<>(updateRequest),
                    DistrictResponse.class);

            districtResponse = responseEntity.getBody();

            assertNotNull(districtResponse);
            assertTrue(responseEntity.getStatusCode().isSameCodeAs(HttpStatus.OK));
            assertEquals(districtResponse.getName(), name.getStringConvert());

            verify(districtRepository, times(1)).findById(any(UUID.class));
            verify(districtRepository, times(1)).existsDistinctByName(name);
            verify(updateDistrictMapper, times(1)).updateDistrictData(any(UpdateDistrictRequest.class), any(District.class));
            verify(districtRepository, times(1)).save(any(District.class));
            verify(findDistrictMapper, times(1)).entityToResponse(any(District.class));
        }

        @Test
        @Tag("E2E")
        @DisplayName("Тест на выброс IdNotFoundException")
        void updateDistrictIdNotFoundTest() {

            responseError = restTemplate.exchange(
                    URL + "/" + badId,
                    HttpMethod.PUT,
                    new HttpEntity<>(updateRequest),
                    ErrorResponse.class);

            assertTrue(responseError.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND));
            assertEquals("Id '" + badId + "' не найден", responseError.getBody().getMessage());

            verify(districtRepository, times(1)).findById(any(UUID.class));
            verify(districtRepository, never()).existsDistinctByName(any(DistrictNameEnum.class));
            verify(updateDistrictMapper, never()).updateDistrictData(any(UpdateDistrictRequest.class), any(District.class));
            verify(districtRepository, never()).save(any(District.class));
            verify(findDistrictMapper, never()).entityToResponse(any(District.class));

        }

        @ParameterizedTest
        @EnumSource(
                value = DistrictNameEnum.class,
                mode = EnumSource.Mode.INCLUDE,
                names = {"CENTRALNY"}
        )
        @Tag("E2E")
        @DisplayName("Тест на выброс PositionOccupiedException")
        void updateDistrictPositionOccupiedTest(DistrictNameEnum name) {

            updateRequest.setName(name);

            responseError = restTemplate.exchange(
                    URL + "/" + id1,
                    HttpMethod.PUT,
                    new HttpEntity<>(updateRequest),
                    ErrorResponse.class);

            assertTrue(responseError.getStatusCode().isSameCodeAs(HttpStatus.CONFLICT));
            assertEquals("Позиция '" + name.getStringConvert() + "' уже занята", responseError.getBody().getMessage());

            verify(districtRepository, times(1)).findById(any(UUID.class));
            verify(districtRepository, times(1)).existsDistinctByName(name);
            verify(updateDistrictMapper, never()).updateDistrictData(any(UpdateDistrictRequest.class), any(District.class));
            verify(districtRepository, never()).save(any(District.class));
            verify(findDistrictMapper, never()).entityToResponse(any(District.class));
        }
    }

    @Nested
    @DisplayName("Тесты на метод deleteDistrict удаления района по id")
    class DeleteDistrictByIdTests {

        @Test
        @Transactional
        @Tag("E2E")
        @DisplayName("Тест с позитивным исходом")
        void deleteDistrictByIdTest() {

            districtService.deleteDistrict(UUID.fromString("3d30f1c3-e70d-42a0-a3d3-58a5c2d50d04"));

            verify(districtRepository, times(1)).findById(any(UUID.class));
            verify(districtRepository, times(1)).deleteById(any(UUID.class));
        }

        @Test
        @Tag("E2E")
        @DisplayName("Тест на выброс IdNotFoundException")
        void deleteDistrictIdNotFoundTest() {

            IdNotFoundException exception = assertThrows(
                    IdNotFoundException.class,
                    () -> districtService.deleteDistrict(badId)
            );

            assertEquals("Id '" + badId + "' не найден", exception.getMessage());

            verify(districtRepository, times(1)).findById(any(UUID.class));
            verify(districtRepository, never()).deleteById(any(UUID.class));
        }

        @Test
        @Tag("E2E")
        @DisplayName("Тест на выброс DeleteDistrictException")
        void deleteDistrictByIdDeleteDistrictExceptionTest() {

            district.setSections(List.of(new Section()));

            DeleteDistrictException exception = assertThrows(
                    DeleteDistrictException.class,
                    () -> districtService.deleteDistrict(id1)
            );

            assertFalse(district.getSections().isEmpty());
            assertEquals(district.getName().getStringConvert() + " содержит участки, удаление запрещено",exception.getMessage());

            verify(districtRepository, times(1)).findById(any(UUID.class));
            verify(districtRepository, never()).deleteById(any(UUID.class));
        }
    }
}
