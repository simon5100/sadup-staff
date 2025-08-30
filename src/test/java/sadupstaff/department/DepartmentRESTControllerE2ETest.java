package sadupstaff.department;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import sadupstaff.dto.request.create.CreateDepartmentRequest;
import sadupstaff.dto.request.update.UpdateDepartmentRequest;
import sadupstaff.dto.response.DepartmentResponse;
import sadupstaff.entity.management.Department;
import sadupstaff.enums.DepartmentNameEnum;
import sadupstaff.exception.ErrorResponse;
import sadupstaff.mapper.department.CreateDepartmentMapper;
import sadupstaff.mapper.department.FindDepartmentMapper;
import sadupstaff.mapper.department.UpdateDepartmentMapper;
import sadupstaff.repository.DepartmentRepository;
import sadupstaff.service.department.DepartmentService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;
import static sadupstaff.enums.DepartmentNameEnum.FINANCE_AND_PLANNING;
import static sadupstaff.enums.DepartmentNameEnum.LEGAL_SUPPORT;

@Log4j2
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@DisplayName("E2E тесты методов DepartmentRESTControllerImpl")
public class DepartmentRESTControllerE2ETest {

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
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @MockitoSpyBean
    private DepartmentRepository departmentRepository;

    @MockitoSpyBean
    private DepartmentService departmentService;

    @MockitoSpyBean
    private UpdateDepartmentMapper updateDepartmentMapper;

    @MockitoSpyBean
    private FindDepartmentMapper findDepartmentMapper;

    @MockitoSpyBean
    private CreateDepartmentMapper createDepartmentMapper;

    private Department department;

    private UUID id1;

    private UUID id2;

    private UUID badId;

    private CreateDepartmentRequest createDepartmentRequest;

    private UpdateDepartmentRequest updateDepartmentRequest;

    private DepartmentResponse response;

    private static String URL = "/api/v1/departments";

    private ResponseEntity<DepartmentResponse> responseEntity;

    private ResponseEntity<ErrorResponse> responseError;

    @BeforeEach
    void setUp(){


        responseEntity = null;
        responseError = null;

        department = new Department(
                UUID.fromString("2d30f1c3-e70d-42a0-a3d3-58a5c2d50d04"),
                LEGAL_SUPPORT,
                5,
                "обеспечивает правами",
                LocalDateTime.of(2025,07,30, 15,17,00,000),
                LocalDateTime.of(2025,07,30, 15,17,00,000),
                List.of()
        );

        id1 = department.getId();
        id2 = UUID.fromString("3d30f1c3-e70d-42a0-a3d3-58a5c2d50d04");
        badId = UUID.fromString("2d30f1c3-e70d-42a0-a3d3-58a5c2d50d05");

        createDepartmentRequest = new CreateDepartmentRequest(
                FINANCE_AND_PLANNING,
                5,
                "обеспечивает деньгами"
        );

        response = new DepartmentResponse();
        response.setName(department.getName().getStringConvert());
        response.setDescription(department.getDescription());
        response.setUpdatedAt(department.getUpdatedAt());
        response.setCreatedAt(department.getCreatedAt());
        response.setEmps(List.of());

        updateDepartmentRequest = new UpdateDepartmentRequest();

        log.info("create db and data");
    }

    @Nested
    @Order(1)
    @DisplayName("Тесты на метод showAllDepartments поиска всех отделов")
    class ShowAllDepartmentsTests {

        @Test
        @Tag("E2E")
        @DisplayName("Тест с позитивным исходом")
        void showAllDepartmentsTest() {

            DepartmentResponse[] departmentResponses;

            ResponseEntity<DepartmentResponse[]> responseList = restTemplate.getForEntity(URL, DepartmentResponse[].class);
            departmentResponses = responseList.getBody();

            assertNotNull(responseList);
            assertEquals(responseList.getStatusCode(), HttpStatus.OK);
            assertEquals(2, departmentResponses.length);
            assertEquals(LEGAL_SUPPORT.getStringConvert(), departmentResponses[0].getName());
            assertEquals(FINANCE_AND_PLANNING.getStringConvert(), departmentResponses[1].getName());
            assertEquals(1, departmentResponses[0].getEmps().size());
            assertEquals(0, departmentResponses[1].getEmps().size());

            verify(departmentService, times(1)).getAllDepartments();
            verify(departmentRepository, times(1)).findAll();
            verify(findDepartmentMapper, times(2)).entityToResponse(any(Department.class));
        }
    }

    @Nested
    @Order(2)
    @DisplayName("Тесты на метод getDepartment поиска отдела по id")
    class GetDepartmentTests {

        @ParameterizedTest
        @CsvSource({
                "LEGAL_SUPPORT, 2d30f1c3-e70d-42a0-a3d3-58a5c2d50d04, 1",
                "FINANCE_AND_PLANNING, 3d30f1c3-e70d-42a0-a3d3-58a5c2d50d04, 0"
        })
        @Tag("E2E")
        @DisplayName("Тест с позитивным исходом")
        void getDepartmentTest(DepartmentNameEnum name, UUID id, int emps) {

            responseEntity = restTemplate.getForEntity(URL + "/" + id, DepartmentResponse.class);

            DepartmentResponse departmentResponse = responseEntity.getBody();

            assertNotNull(responseEntity);
            assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
            assertEquals(departmentResponse.getName(), name.getStringConvert());
            assertEquals(departmentResponse.getEmps().size(), emps);

            verify(departmentService, times(1)).getDepartmentById(id);
            verify(departmentRepository, times(1)).findById(id);
            verify(findDepartmentMapper, times(1)).entityToResponse(any(Department.class));
        }

        @Test
        @Tag("E2E")
        @DisplayName("Тест с выбросом IdNotFoundException")
        void getDepartmentNotFoundIdTest() {

            responseError = restTemplate.getForEntity(URL + "/" + badId, ErrorResponse.class);

            assertTrue(responseError.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND));
            assertEquals("Id '" + badId + "' не найден", responseError.getBody().getMessage());

            verify(departmentService, times(1)).getDepartmentById(badId);
            verify(departmentRepository, times(1)).findById(badId);
        }
    }

    @Nested
    @Order(4)
    @DisplayName("Тесты на метод addDepartment сохранения отдела")
    class AddDepartmentTests {

        @ParameterizedTest
        @CsvSource({
                "CIVIL_SERVICE_AND_HR, Отдел государственной гражданской службы и кадров",
                "FINANCE_AND_PLANNING, Отдел финансирования и планирования"
        })
        @Tag("E2E")
        @DisplayName("Тест с позитивным исходом")
        void addDepartment(DepartmentNameEnum createName, String responseName) {

            createDepartmentRequest.setName(createName);

            responseEntity = restTemplate.postForEntity(URL, createDepartmentRequest, DepartmentResponse.class);
            DepartmentResponse responseCheck = responseEntity.getBody();

            assertNotNull(responseEntity);
            assertTrue(responseEntity.getStatusCode().isSameCodeAs(HttpStatus.OK));
            assertEquals(responseCheck.getName(), responseName);

            verify(departmentService, times(1)).saveDepartment(any(CreateDepartmentRequest.class));
            verify(departmentRepository, times(1)).existsDistinctByName(createName);
            verify(createDepartmentMapper, times(1)).toEntity(any(CreateDepartmentRequest.class));
            verify(departmentRepository, times(1)).save(any(Department.class));
            verify(findDepartmentMapper, times(1)).entityToResponse(any(Department.class));

        }

        @ParameterizedTest
        @EnumSource(value = DepartmentNameEnum.class,
                mode = EnumSource.Mode.INCLUDE,
                names = {"LEGAL_SUPPORT", "LOGISTICS_SUPPORT"})
        @Tag("E2E")
        @DisplayName("Тест на выброс PositionOccupiedException")
        void addDepartmentPositionOccupiedTest(DepartmentNameEnum name){

            createDepartmentRequest.setName(name);

            responseError = restTemplate.postForEntity(URL, createDepartmentRequest, ErrorResponse.class);

            assertNotNull(responseError);
            assertTrue(responseError.getStatusCode().isSameCodeAs(HttpStatus.CONFLICT));
            assertEquals("Позиция '" + name.getStringConvert() + "' уже занята", responseError.getBody().getMessage());

            verify(departmentService, times(1)).saveDepartment(any(CreateDepartmentRequest.class));
            verify(departmentRepository,times(1)).existsDistinctByName(name);
            verify(createDepartmentMapper, never()).toEntity(any(CreateDepartmentRequest.class));
            verify(departmentRepository, never()).save(any(Department.class));
            verify(findDepartmentMapper, never()).entityToResponse(any(Department.class));
        }
    }

    @Nested
    @Order(3)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("Тесты на метод updateDepartment обновления данных отдела")
    class UpdateDepartmentTests {

        @ParameterizedTest
        @CsvSource({
                "CIVIL_SERVICE_AND_HR, кадры, 2d30f1c3-e70d-42a0-a3d3-58a5c2d50d04",
                "LOGISTICS_SUPPORT, два степлера, 2d30f1c3-e70d-42a0-a3d3-58a5c2d50d04",
                "CIVIL_SERVICE_AND_HR, кадры, 3d30f1c3-e70d-42a0-a3d3-58a5c2d50d04",
                "LEGAL_SUPPORT, права, 3d30f1c3-e70d-42a0-a3d3-58a5c2d50d04"
        })
        @Tag("E2E")
        @DisplayName("Тест с позитивным исходом")
        void updateDepartmentTest(DepartmentNameEnum name, String description, UUID id) {

            updateDepartmentRequest.setName(name);
            updateDepartmentRequest.setDescription(description);

            responseEntity = restTemplate.exchange(URL + "/" + id,
                    HttpMethod.PUT,
                    new HttpEntity<>(updateDepartmentRequest),  DepartmentResponse.class);

            DepartmentResponse responseCheck = responseEntity.getBody();

            assertNotNull(responseCheck);
            assertTrue(responseEntity.getStatusCode().isSameCodeAs(HttpStatus.OK));
            assertEquals(name.getStringConvert(), responseCheck.getName());

            verify(departmentService, times(1)).updateDepartment(id, updateDepartmentRequest);
            verify(departmentRepository, times(1)).existsDistinctByName(name);
            verify(updateDepartmentMapper, times(1)).updateDepartmentData(any(UpdateDepartmentRequest.class), any(Department.class));
            verify(departmentRepository, times(1)).save(any(Department.class));
            verify(findDepartmentMapper, times(1)).entityToResponse(any(Department.class));
        }

        @Test
        @Tag("E2E")
        @DisplayName("Тест на выброс IdNotFoundException")
        void updateDepartmentIdNotFoundTest() {

            ResponseEntity<ErrorResponse> responseError = restTemplate.exchange(
                    URL + "/" + badId,
                    HttpMethod.PUT,
                    new HttpEntity<>(updateDepartmentRequest),
                    ErrorResponse.class);


            assertTrue(responseError.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND));
            assertEquals("Id '" + badId + "' не найден", responseError.getBody().getMessage());

            verify(departmentService, times(1)).updateDepartment(badId, updateDepartmentRequest);
            verify(departmentRepository, times(1)).findById(badId);
            verify(updateDepartmentMapper, never()).updateDepartmentData(any(UpdateDepartmentRequest.class), any(Department.class));
            verify(departmentRepository, never()).existsDistinctByName(department.getName());
            verify(departmentRepository, never()).save(any(Department.class));
            verify(findDepartmentMapper, never()).entityToResponse(any(Department.class));

        }

        @ParameterizedTest
        @EnumSource(value = DepartmentNameEnum.class,
                mode = EnumSource.Mode.INCLUDE,
                names = {"LEGAL_SUPPORT", "FINANCE_AND_PLANNING"})
        @Tag("E2E")
        @Order(1)
        @DisplayName("Тест на выброс PositionOccupiedException")
        void updateDepartmentPositionOccupiedTest(DepartmentNameEnum name) {

            updateDepartmentRequest.setName(name);

            ResponseEntity<ErrorResponse> responseError = restTemplate.exchange(
                    URL + "/" + id1,
                    HttpMethod.PUT,
                    new HttpEntity<>(updateDepartmentRequest),
                    ErrorResponse.class);

            assertTrue(responseError.getStatusCode().isSameCodeAs(HttpStatus.CONFLICT));
            assertEquals("Позиция '" + name.getStringConvert() + "' уже занята", responseError.getBody().getMessage());

            verify(departmentService, times(1)).updateDepartment(id1, updateDepartmentRequest);
            verify(departmentRepository, times(1)).findById(id1);
            verify(departmentRepository, times(1)).existsDistinctByName(name);
            verify(updateDepartmentMapper, never()).updateDepartmentData(any(UpdateDepartmentRequest.class), any(Department.class));
            verify(departmentRepository, never()).save(any(Department.class));
            verify(findDepartmentMapper, never()).entityToResponse(any(Department.class));
        }
    }

    @Nested
    @Order(5)
    @DisplayName("Тесты на метод deleteDepartment удаления отделя по id1")
    class DeleteDepartmentByIdTests {

        @Test
        @Tag("E2E")
        @DisplayName("Тест с позитивным исходом")
        void deleteDepartmentByIdTest() {

            ResponseEntity<Void> status = restTemplate.exchange(
                    URL + "/" + id2,
                    HttpMethod.DELETE,
                    new HttpEntity<>(Void.class),
                    Void.class);

            assertTrue(status.getStatusCode().isSameCodeAs(HttpStatus.OK));

            verify(departmentService, times(1)).deleteDepartment(id2);
            verify(departmentRepository, times(1)).findById(id2);
            verify(departmentRepository, times(1)).deleteById(id2);
        }

        @Test
        @Tag("E2E")
        @DisplayName("Тест на выброс IdNotFoundException")
        void deleteDepartmentIdNotFoundTest() {

            ResponseEntity<ErrorResponse> status = restTemplate.exchange(
                    URL + "/" + badId,
                    HttpMethod.DELETE,
                    new HttpEntity<>(ErrorResponse.class),
                    ErrorResponse.class);

            assertTrue(status.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND));
            assertEquals("Id '" + badId + "' не найден", status.getBody().getMessage());

            verify(departmentService, times(1)).deleteDepartment(badId);
            verify(departmentRepository, times(1)).findById(badId);
            verify(departmentRepository, never()).deleteById(badId);
        }

        @Test
        @Tag("E2E")
        @DisplayName("Тест на выброс DeleteDepartmentException")
        void deleteDepartmentByIdDeleteDepartmentExceptionTest() {

            ResponseEntity<ErrorResponse> status = restTemplate.exchange(
                    URL + "/" + id1,
                    HttpMethod.DELETE,
                    new HttpEntity<>(ErrorResponse.class),
                    ErrorResponse.class);

            assertTrue(status.getStatusCode().isSameCodeAs(HttpStatus.UNPROCESSABLE_ENTITY));
            assertEquals(DepartmentNameEnum.LOGISTICS_SUPPORT.getStringConvert() + " имеет сотрудников", status.getBody().getMessage());

            verify(departmentService, times(1)).deleteDepartment(id1);
            verify(departmentRepository, times(1)).findById(id1);
            verify(departmentRepository, never()).deleteById(id1);
        }
    }
}