package sadupstaff.employee;

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
import sadupstaff.dto.request.create.CreateEmployeeRequest;
import sadupstaff.dto.request.update.UpdateEmployeeRequest;
import sadupstaff.dto.response.EmployeeResponse;
import sadupstaff.entity.management.Employee;
import sadupstaff.enums.DepartmentNameEnum;
import sadupstaff.enums.PositionEmployeeEnum;
import sadupstaff.exception.ErrorResponse;
import sadupstaff.mapper.employee.CreateEmployeeMapper;
import sadupstaff.mapper.employee.FindEmployeeMapper;
import sadupstaff.mapper.employee.UpdateEmployeeMapper;
import sadupstaff.repository.EmployeeRepository;
import sadupstaff.service.department.DepartmentServiceImpl;
import sadupstaff.service.employee.EmployeeServiceImpl;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;
import static sadupstaff.enums.DepartmentNameEnum.LEGAL_SUPPORT;
import static sadupstaff.enums.PositionEmployeeEnum.CONSULTANT;
import static sadupstaff.enums.PositionEmployeeEnum.SENIOR_SPECIALIST;

@Log4j2
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
@DisplayName("E2E тесты методов EmployeeServiceImpl")
public class EmployeeServiceImplE2ETest {

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
    private EmployeeRepository employeeRepository;

    @MockitoSpyBean
    private DepartmentServiceImpl departmentService;

    @MockitoSpyBean
    private UpdateEmployeeMapper updateEmployeeMapper;

    @MockitoSpyBean
    private FindEmployeeMapper findEmployeeMapper;

    @MockitoSpyBean
    private CreateEmployeeMapper createEmployeeMapper;

    @MockitoSpyBean
    private EmployeeServiceImpl employeeService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestRestTemplate restTemplate;

    private static String URL = "/api/v1/employees";

    private CreateEmployeeRequest createRequest;

    private UpdateEmployeeRequest updateRequest;

    private EmployeeResponse response;

    private UUID id = UUID.fromString("6d30f1c3-e70d-42a0-a3d3-58a5c2d50d04");

    private UUID badId = UUID.randomUUID();

    private ResponseEntity<EmployeeResponse> responseEntity;

    private ResponseEntity<ErrorResponse> responseError;

    private void initializeSchema() {
        jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS sudstaff");
        log.info("Schema sudstaff created");
    }

    private void initializeTables() {
        jdbcTemplate.execute(
                "CREATE TABLE IF not EXISTS sudstaff.departments (\n" +
                        "id UUID     PRIMARY KEY,\n" +
                        "name        VARCHAR(255)    not null,\n" +
                        "description TEXT,\n" +
                        "created_at  TIMESTAMP       not null,\n" +
                        "updated_at  TIMESTAMP       not null," +
                        "max_number_employees    INTEGER    NOT NULL   DEFAULT 5" +
                        ");"

        );

        jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS sudstaff.employees (\n" +
                        "id              UUID            PRIMARY KEY,\n" +
                        "personel_number VARCHAR(255)    NOT NULL,\n" +
                        "first_name      VARCHAR(255)    NOT NULL,\n" +
                        "last_name       VARCHAR(255)    NOT NULL,\n" +
                        "patronymic      VARCHAR(255),\n" +
                        "position        VARCHAR(255)    NOT NULL,\n" +
                        "created_at      TIMESTAMP       NOT NULL,\n" +
                        "updated_at      TIMESTAMP       NOT NULL,\n" +
                        "department_id   UUID            NOT NULL,\n" +
                        "\n" +
                        "CONSTRAINT fk_department\n" +
                        "FOREIGN KEY (department_id)\n" +
                        "   REFERENCES sudstaff.departments(id)\n" +
                        "   ON DELETE CASCADE" +
                        ");"
        );

        log.info("tables created");
    }

    private void initializeData() {
        jdbcTemplate.execute(
                "insert into sudstaff.departments (id, name, description, created_at, updated_at, max_number_employees)\n" +
                        "values (\n" +
                        "'2d30f1c3-e70d-42a0-a3d3-58a5c2d50d04',\n" +
                        "'LEGAL_SUPPORT',\n" +
                        "'обеспечивает правами',\n" +
                        "'2025.07.30 15:17:00',\n" +
                        "'2025.07.30 15:17:00',\n" +
                        "5\n" +
                        ");"
        );

        jdbcTemplate.execute(
                "insert into sudstaff.employees (id, personel_number, first_name, last_name, patronymic, position, created_at, updated_at, department_id)\n" +
                        "values (\n" +
                        "'6d30f1c3-e70d-42a0-a3d3-58a5c2d50d04',\n" +
                        "'EMP12345',\n" +
                        "'Иван',\n" +
                        "'Иванов',\n" +
                        "'Иванович',\n" +
                        "'CONSULTANT',\n" +
                        "'2025.07.30 15:17:00',\n" +
                        "'2025.07.30 15:17:00',\n" +
                        "'2d30f1c3-e70d-42a0-a3d3-58a5c2d50d04'" +
                        ");"
        );

        log.info("Добавление данных");
    }

    @BeforeEach
    void setUp() {

        initializeSchema();
        initializeTables();

        jdbcTemplate.execute("Delete from sudstaff.employees");
        jdbcTemplate.execute("Delete from sudstaff.departments");

        log.info("очистка таблиц");

        responseEntity = null;
        responseError = null;

        initializeData();

        createRequest = new CreateEmployeeRequest(
                "EMP12344",
                "Иван",
                "Иванов",
                "Иванович",
                SENIOR_SPECIALIST,
                DepartmentNameEnum.LEGAL_SUPPORT
        );

        updateRequest = new UpdateEmployeeRequest();
    }

    @Nested
    @DisplayName("Тесты на метод getAllEmployees поиска всех сотрудников")
    class GetAllEmployeeTests {

        @Test
        @Tag("E2E")
        @DisplayName("Тест с позитивным исходом")
        void getAllEmployeesTest() {

            EmployeeResponse[] employeeResponses;
            ResponseEntity<EmployeeResponse[]> responseEntityArr = restTemplate.getForEntity(URL, EmployeeResponse[].class);
            employeeResponses = responseEntityArr.getBody();

            assertNotNull(employeeResponses);
            assertTrue(responseEntityArr.getStatusCode().isSameCodeAs(HttpStatus.OK));
            assertEquals(1, employeeResponses.length);
            assertEquals(employeeResponses[0].getPosition(), CONSULTANT.getStringConvert());
            assertEquals(employeeResponses[0].getDepartmentName(), LEGAL_SUPPORT.getStringConvert());

            verify(employeeRepository, times(1)).findAll();
            verify(findEmployeeMapper, times(1)).entityToResponse(any(Employee.class));
        }
    }

    @Nested
    @DisplayName("Тесты на метод getEmployee поиска сотрудника по id")
    class GetEmployeeByIdTests {

        @Test
        @Tag("E2E")
        @DisplayName("Тест с позитивным исходом")
        void getEmployeeByIdTest() {

            responseEntity = restTemplate.getForEntity(URL + "/" +  id, EmployeeResponse.class);
            response = responseEntity.getBody();

            assertNotNull(response);
            assertEquals(response.getDepartmentName(), LEGAL_SUPPORT.getStringConvert());
            assertEquals(response.getPosition(), CONSULTANT.getStringConvert());

            verify(employeeRepository, times(1)).findById(id);
            verify(findEmployeeMapper, times(1)).entityToResponse(any(Employee.class));
        }

        @Test
        @Tag("E2E")
        @DisplayName("Тест с выбросом IdNotFoundException")
        void getEmployeeByIdNotFoundIdTest() {

            responseError = restTemplate.getForEntity(URL + "/" +  badId, ErrorResponse.class);

            assertTrue(responseError.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND));
            assertEquals("Id '" + badId + "' не найден", responseError.getBody().getMessage());

            verify(employeeRepository, times(1)).findById(any(UUID.class));
            verify(findEmployeeMapper, never()).entityToResponse(any(Employee.class));
        }
    }

    @Nested
    @DisplayName("Тесты на метод saveEmployee сохранения сотрудника")
    class SaveEmployeeTests {

        @ParameterizedTest
        @EnumSource(value = PositionEmployeeEnum.class,
                mode = EnumSource.Mode.INCLUDE,
                names = {"HEAD_OF_DEPARTMENT",
                        "DEPUTY_HEAD_OF_DEPARTMENT",
                        "DEPARTMENT_HEAD",
                        "DEPUTY_DEPARTMENT_HEAD"})
        @Tag("E2E")
        @DisplayName("Тест с позитивным исходом")
        void saveEmployeeTest(PositionEmployeeEnum position) {

            createRequest.setPosition(position);

            responseEntity = restTemplate.postForEntity(URL, createRequest, EmployeeResponse.class);
            response = responseEntity.getBody();

            assertTrue(responseEntity.getStatusCode().isSameCodeAs(HttpStatus.OK));
            assertNotNull(response);
            assertEquals(response.getPosition(), position.getStringConvert());

            verify(createEmployeeMapper, times(1)).toEntity(createRequest);
            verify(departmentService, times(1)).getDepartmentByName(createRequest.getDepartmentName());
            verify(employeeRepository, times(1)).findById(any(UUID.class));
            verify(employeeRepository, times(1)).save(any(Employee.class));
            verify(findEmployeeMapper, times(1)).entityToResponse(any(Employee.class));
        }

        @ParameterizedTest
        @EnumSource(value = PositionEmployeeEnum.class,
                mode = EnumSource.Mode.INCLUDE,
                names = {"CONSULTANT"}
        )
        @Tag("E2E")
        @DisplayName("Тест на выброс PositionOccupiedException")
        void saveEmployeePositionOccupiedTest(PositionEmployeeEnum position) {

            createRequest.setPosition(position);

            responseError = restTemplate.postForEntity(URL, createRequest, ErrorResponse.class);


            assertNotNull(responseError);
            assertTrue(responseError.getStatusCode().isSameCodeAs(HttpStatus.CONFLICT));
            assertEquals("Позиция '" + position.getStringConvert() + "' уже занята", responseError.getBody().getMessage());

            verify(createEmployeeMapper, times(1)).toEntity(createRequest);
            verify(departmentService, times(1)).getDepartmentByName(createRequest.getDepartmentName());
            verify(employeeRepository, never()).findById(any(UUID.class));
            verify(employeeRepository, never()).save(any(Employee.class));
            verify(findEmployeeMapper, never()).entityToResponse(any(Employee.class));
        }

        @ParameterizedTest
        @EnumSource(value = PositionEmployeeEnum.class,
                mode = EnumSource.Mode.INCLUDE,
                names = {"LEAD_EXPERT"}
        )
        @Tag("E2E")
        @DisplayName("Тест на выброс MaxEmployeeInDepartmentException")
        void saveEmployeeMaxEmployeeInDepartmentTest(PositionEmployeeEnum position) {
            jdbcTemplate.execute(
                    "insert into sudstaff.employees (id, personel_number, first_name, last_name, patronymic, position, created_at, updated_at, department_id)\n" +
                            "values (\n" +
                            "'7d30f1c3-e70d-42a0-a3d3-58a5c2d50d04',\n" +
                            "'EMP12345',\n" +
                            "'Иван',\n" +
                            "'Иванов',\n" +
                            "'Иванович',\n" +
                            "'HEAD_OF_DEPARTMENT',\n" +
                            "'2025.07.30 15:17:00',\n" +
                            "'2025.07.30 15:17:00',\n" +
                            "'2d30f1c3-e70d-42a0-a3d3-58a5c2d50d04'" +
                            ");"
            );

            jdbcTemplate.execute(
                    "insert into sudstaff.employees (id, personel_number, first_name, last_name, patronymic, position, created_at, updated_at, department_id)\n" +
                            "values (\n" +
                            "'8d30f1c3-e70d-42a0-a3d3-58a5c2d50d04',\n" +
                            "'EMP12345',\n" +
                            "'Иван',\n" +
                            "'Иванов',\n" +
                            "'Иванович',\n" +
                            "'DEPUTY_HEAD_OF_DEPARTMENT',\n" +
                            "'2025.07.30 15:17:00',\n" +
                            "'2025.07.30 15:17:00',\n" +
                            "'2d30f1c3-e70d-42a0-a3d3-58a5c2d50d04'" +
                            ");"
            );

            jdbcTemplate.execute(
                    "insert into sudstaff.employees (id, personel_number, first_name, last_name, patronymic, position, created_at, updated_at, department_id)\n" +
                            "values (\n" +
                            "'9d30f1c3-e70d-42a0-a3d3-58a5c2d50d04',\n" +
                            "'EMP12345',\n" +
                            "'Иван',\n" +
                            "'Иванов',\n" +
                            "'Иванович',\n" +
                            "'DEPARTMENT_HEAD',\n" +
                            "'2025.07.30 15:17:00',\n" +
                            "'2025.07.30 15:17:00',\n" +
                            "'2d30f1c3-e70d-42a0-a3d3-58a5c2d50d04'" +
                            ");"
            );

            jdbcTemplate.execute(
                    "insert into sudstaff.employees (id, personel_number, first_name, last_name, patronymic, position, created_at, updated_at, department_id)\n" +
                            "values (\n" +
                            "'6d40f1c3-e70d-42a0-a3d3-58a5c2d50d04',\n" +
                            "'EMP12345',\n" +
                            "'Иван',\n" +
                            "'Иванов',\n" +
                            "'Иванович',\n" +
                            "'DEPUTY_DEPARTMENT_HEAD',\n" +
                            "'2025.07.30 15:17:00',\n" +
                            "'2025.07.30 15:17:00',\n" +
                            "'2d30f1c3-e70d-42a0-a3d3-58a5c2d50d04'" +
                            ");"
            );

            createRequest.setPosition(position);

            responseError = restTemplate.postForEntity(URL, createRequest, ErrorResponse.class);

            assertNotNull(responseError);
            assertTrue(responseError.getStatusCode().isSameCodeAs(HttpStatus.CONFLICT));
            assertEquals("В '" + LEGAL_SUPPORT.getStringConvert() + "' максимальное количество сотрудников", responseError.getBody().getMessage());

            verify(createEmployeeMapper, times(1)).toEntity(any(CreateEmployeeRequest.class));
            verify(departmentService, times(1)).getDepartmentByName(any(DepartmentNameEnum.class));
            verify(employeeRepository, never()).findById(any(UUID.class));
            verify(employeeRepository, never()).save(any(Employee.class));
            verify(findEmployeeMapper, never()).entityToResponse(any(Employee.class));
        }
    }

    @Nested
    @DisplayName("Тесты на метод updateEmployee обновления данных сотрудника")
    class UpdateEmployeeTests {

        @ParameterizedTest
        @EnumSource(value = PositionEmployeeEnum.class,
                mode = EnumSource.Mode.EXCLUDE,
                names = {"CONSULTANT"}
        )
        @Tag("E2E")
        @DisplayName("Тест с позитивным исходом")
        void updateEmployeeTest(PositionEmployeeEnum position) {

            updateRequest.setPosition(position);

            responseEntity = restTemplate.exchange(
                    URL + "/" + id,
                    HttpMethod.PUT,
                    new HttpEntity<>(updateRequest),
                    EmployeeResponse.class);

            response = responseEntity.getBody();

            assertNotNull(response);
            assertTrue(responseEntity.getStatusCode().isSameCodeAs(HttpStatus.OK));
            assertEquals(response.getPosition(), position.getStringConvert());

            verify(employeeRepository, times(1)).findById(any(UUID.class));
            verify(employeeRepository, times(1)).existsEmployeeByPosition(position);
            verify(updateEmployeeMapper, times(1)).updateEmployeeData(any(UpdateEmployeeRequest.class), any(Employee.class));
            verify(employeeRepository, times(1)).save(any(Employee.class));
            verify(findEmployeeMapper, times(1)).entityToResponse(any(Employee.class));
        }

        @Test
        @Tag("E2E")
        @DisplayName("Тест на выброс IdNotFoundException")
        void updateEmployeeIdNotFoundTest() {

            responseError = restTemplate.exchange(
                    URL + "/" + badId,
                    HttpMethod.PUT,
                    new HttpEntity<>(updateRequest),
                    ErrorResponse.class);

            assertTrue(responseError.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND));
            assertEquals("Id '" + badId + "' не найден", responseError.getBody().getMessage());

            verify(employeeRepository, times(1)).findById(badId);
            verify(employeeRepository, never()).existsEmployeeByPosition(any(PositionEmployeeEnum.class));
            verify(updateEmployeeMapper, never()).updateEmployeeData(any(UpdateEmployeeRequest.class), any(Employee.class));
            verify(employeeRepository, never()).save(any(Employee.class));
            verify(findEmployeeMapper, never()).entityToResponse(any(Employee.class));
        }

        @ParameterizedTest
        @EnumSource(value = PositionEmployeeEnum.class,
                mode = EnumSource.Mode.INCLUDE,
                names = {"CONSULTANT"}
        )
        @Tag("E2E")
        @DisplayName("Тест на выброс PositionOccupiedException")
        void updateEmployeePositionOccupiedTest(PositionEmployeeEnum position) {

            updateRequest.setPosition(position);

            responseError = restTemplate.exchange(
                    URL + "/" + id,
                    HttpMethod.PUT,
                    new HttpEntity<>(updateRequest),
                    ErrorResponse.class);

            assertTrue(responseError.getStatusCode().isSameCodeAs(HttpStatus.CONFLICT));
            assertEquals("Позиция '" + position.getStringConvert() + "' уже занята", responseError.getBody().getMessage());

            verify(employeeRepository, times(1)).findById(any(UUID.class));
            verify(employeeRepository, times(1)).existsEmployeeByPosition(any(PositionEmployeeEnum.class));
            verify(updateEmployeeMapper, never()).updateEmployeeData(any(UpdateEmployeeRequest.class), any(Employee.class));
            verify(employeeRepository, never()).save(any(Employee.class));
            verify(findEmployeeMapper, never()).entityToResponse(any(Employee.class));
        }
    }

    @Nested
    @DisplayName("Тесты на метод deleteEmployee удаления сотрудника по id")
    class DeleteEmployeeTests {

        @Test
        @Tag("E2E")
        @DisplayName("Тест с позитивным исходом")
        void deleteEmployeeByIdTest() {

            ResponseEntity<Void> status = restTemplate.exchange(
                    URL + "/" + id,
                    HttpMethod.DELETE,
                    new HttpEntity<>(Void.class),
                    Void.class
            );

            assertTrue(status.getStatusCode().isSameCodeAs(HttpStatus.OK));
            verify(employeeRepository, times(1)).findById(id);
            verify(employeeRepository, times(1)).deleteById(id);
        }

        @Test
        @Tag("E2E")
        @DisplayName("Тест на выброс IdNotFoundException")
        void deleteEmployeeIdNotFoundTest() {

            ResponseEntity<ErrorResponse> status = restTemplate.exchange(
                    URL + "/" + badId,
                    HttpMethod.DELETE,
                    new HttpEntity<>(ErrorResponse.class),
                    ErrorResponse.class);

            assertTrue(status.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND));
            assertEquals("Id '" + badId + "' не найден", status.getBody().getMessage());

            verify(employeeRepository, times(1)).findById(badId);
            verify(employeeRepository, never()).deleteById(badId);
        }
    }
}