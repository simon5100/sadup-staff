package sadupstaff.department;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import sadupstaff.dto.request.create.CreateDepartmentRequest;
import sadupstaff.dto.request.update.UpdateDepartmentRequest;
import sadupstaff.dto.response.DepartmentResponse;
import sadupstaff.entity.management.Department;
import sadupstaff.enums.DepartmentNameEnum;
import sadupstaff.exception.DepartmentNotFoundException;
import sadupstaff.exception.IdNotFoundException;
import sadupstaff.exception.PositionOccupiedException;
import sadupstaff.exception.department.DeleteDepartmentException;
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
import static sadupstaff.enums.DepartmentNameEnum.FINANCE_AND_PLANNING;
import static sadupstaff.enums.DepartmentNameEnum.LEGAL_SUPPORT;

@Log4j2
@SpringBootTest()
@Testcontainers
@Transactional
@DisplayName("Integration тесты методов DepartmentServiceImpl")
class DepartmentServiceImplIntegrationTest {

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

    @MockitoSpyBean
    private DepartmentRepository departmentRepository;

    @Autowired
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

    @BeforeEach
    void setUp(){
        department = new Department(
                UUID.fromString("2d30f1c3-e70d-42a0-a3d3-58a5c2d50d04"),
                LEGAL_SUPPORT,
                5,
                "обеспечивает правами",
                LocalDateTime.now(),
                LocalDateTime.now(),
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

    @AfterEach
    @Transactional
    void tearDown(){
        log.info("clean db");
    }

    @Nested
    @Transactional
    @DisplayName("Тесты на метод getAllDepartments поиска всех отделов")
    class GetAllDepartmentsTests {

        @Test
        @Tag("integration")
        @DisplayName("Тест с позитивным исходом")
        void getAllDepartmentsTest() {

            List<DepartmentResponse> responseList = departmentService.getAllDepartments();

            assertNotNull(responseList);
            assertEquals(2, responseList.size());
            assertEquals(1, responseList.get(0).getEmps().size());
            assertEquals(0, responseList.get(1).getEmps().size());


            verify(departmentRepository, times(1)).findAll();
            verify(findDepartmentMapper, times(2)).entityToResponse(any(Department.class));
        }
    }

    @Nested
    @Transactional
    @DisplayName("Тесты на метод getDepartmentById поиска отдела по id1")
    class GetDepartmentByIdTests {

        @Test
        @Tag("integration")
        @DisplayName("Тест с позитивным исходом")
        void getDepartmentByIdTest() {

            DepartmentResponse responseCheck = departmentService.getDepartmentById(id1);

            assertNotNull(responseCheck);
            assertEquals(responseCheck.getName(), "Отдел правового обеспечения");

            verify(departmentRepository, times(1)).findById(id1);
            verify(findDepartmentMapper, times(1)).entityToResponse(any(Department.class));
        }

        @Test
        @Tag("integration")
        @DisplayName("Тест с выбросом IdNotFoundException")
        void getDepartmentByIdNotFoundIdTest() {

            IdNotFoundException exception = assertThrows(
                    IdNotFoundException.class,
                    () -> departmentService.getDepartmentById(badId));

            assertEquals("Id '" + badId + "' не найден", exception.getMessage());

            verify(departmentRepository, times(1)).findById(badId);
        }
    }

    @Nested
    @Transactional
    @DisplayName("Тесты на метод getDepartmentByName поиска отдела по имени")
    class GetDepartmentByNameTests {

        @ParameterizedTest
        @Tag("integration")
        @EnumSource(value = DepartmentNameEnum.class,
        mode = EnumSource.Mode.INCLUDE,
        names = {"LEGAL_SUPPORT", "FINANCE_AND_PLANNING"})
        @DisplayName("Тест с позитивным исходом")
        void getDepartmentByNameTest(DepartmentNameEnum name) {

            Department departmentCheck = departmentService.getDepartmentByName(name);

            assertNotNull(departmentCheck);
            assertEquals(departmentCheck.getName(), name);

            verify(departmentRepository, times(1)).findDepartmentByName(name);
        }

        @ParameterizedTest
        @EnumSource(value = DepartmentNameEnum.class,
                    mode = EnumSource.Mode.EXCLUDE,
                    names = {"LEGAL_SUPPORT", "FINANCE_AND_PLANNING"})
        @Tag("integration")
        @DisplayName("Тест на выброс DepartmentNotFoundException из-за неверного имени")
        void getDepartmentByNameNotFoundBadNameTest(DepartmentNameEnum name) {

            DepartmentNotFoundException exception = assertThrows(
                    DepartmentNotFoundException.class,
                    () -> departmentService.getDepartmentByName(name)
            );

            assertEquals("Отдела '" + name.getStringConvert() + "' не существует", exception.getMessage());

            verify(departmentRepository, times(1)).findDepartmentByName(name);
        }
    }

    @Nested
    @Transactional
    @DisplayName("Тесты на метод saveDepartment сохранения отдела")
    class SaveDepartmentTests {

        @ParameterizedTest
        @CsvSource({
                "CIVIL_SERVICE_AND_HR, Отдел государственной гражданской службы и кадров",
                "LOGISTICS_SUPPORT, Отдел материально-технического обеспечения"
        })
        @Tag("integrationTest")
        @DisplayName("Тест с позитивным исходом")
        void saveDepartment(DepartmentNameEnum createName, String responseName) {

            response.setName(responseName);
            createDepartmentRequest.setName(createName);

            DepartmentResponse responseCheck = departmentService.saveDepartment(createDepartmentRequest);

            assertNotNull(responseCheck);
            assertEquals(responseCheck.getName(), response.getName());

            verify(departmentRepository, times(1)).existsDistinctByName(createName);
            verify(createDepartmentMapper, times(1)).toEntity(createDepartmentRequest);
            verify(departmentRepository, times(1)).save(any(Department.class));
            verify(findDepartmentMapper, times(1)).entityToResponse(any(Department.class));

            log.info(responseCheck.toString());
        }

        @ParameterizedTest
        @EnumSource(value = DepartmentNameEnum.class,
                    mode = EnumSource.Mode.INCLUDE,
                    names = "LEGAL_SUPPORT")
        @Tag("integrationTest")
        @DisplayName("Тест на выброс PositionOccupiedException")
        void saveDepartmentPositionOccupiedTest(DepartmentNameEnum name){

            createDepartmentRequest.setName(name);

            PositionOccupiedException exception = assertThrows(
                    PositionOccupiedException.class,
                    () -> departmentService.saveDepartment(createDepartmentRequest));

            assertEquals("Позиция '" + name.getStringConvert() + "' уже занята", exception.getMessage());

            verify(departmentRepository,times(1)).existsDistinctByName(name);
            verify(createDepartmentMapper, never()).toEntity(any(CreateDepartmentRequest.class));
            verify(departmentRepository, never()).save(any(Department.class));
            verify(findDepartmentMapper, never()).entityToResponse(any(Department.class));

        }
    }

    @Nested
    @Transactional
    @DisplayName("Тесты на метод updateDepartment обновления данных отдела")
    class UpdateDepartmentTests {

        @ParameterizedTest
        @CsvSource({
                "CIVIL_SERVICE_AND_HR, кадры",
                "LOGISTICS_SUPPORT, два степлера"
        })
        @Tag("integration")
        @DisplayName("Тест с позитивным исходом")
        void updateDepartmentTest(DepartmentNameEnum name, String description) {

            updateDepartmentRequest.setName(name);
            updateDepartmentRequest.setDescription(description);

            DepartmentResponse result = departmentService.updateDepartment(id1, updateDepartmentRequest);

            assertNotEquals(result.getName(), name);

            verify(departmentRepository, times(1)).findById(id1);
            verify(departmentRepository, times(1)).existsDistinctByName(name);
            verify(updateDepartmentMapper, times(1)).updateDepartmentData(any(UpdateDepartmentRequest.class), any(Department.class));
            verify(departmentRepository, times(1)).save(any(Department.class));
            verify(findDepartmentMapper, times(1)).entityToResponse(any(Department.class));
        }

        @Test
        @Tag("integration")
        @DisplayName("Тест на выброс IdNotFoundException")
        void updateDepartmentIdNotFoundTest() {

            IdNotFoundException exception = assertThrows(
                    IdNotFoundException.class,
                    () -> departmentService.updateDepartment(badId, updateDepartmentRequest));

            assertEquals("Id '" + badId + "' не найден", exception.getMessage());

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
        @Tag("integration")
        @DisplayName("Тест на выброс PositionOccupiedException")
        void updateDepartmentPositionOccupiedTest(DepartmentNameEnum name) {

            updateDepartmentRequest.setName(name);

            PositionOccupiedException exception = assertThrows(
                    PositionOccupiedException.class,
                    () -> departmentService.updateDepartment(id1, updateDepartmentRequest)
            );

            assertEquals("Позиция '" + name.getStringConvert() + "' уже занята", exception.getMessage());

            verify(departmentRepository, times(1)).findById(id1);
            verify(departmentRepository, times(1)).existsDistinctByName(name);
            verify(updateDepartmentMapper, never()).updateDepartmentData(any(UpdateDepartmentRequest.class), any(Department.class));
            verify(departmentRepository, never()).save(any(Department.class));
            verify(findDepartmentMapper, never()).entityToResponse(any(Department.class));
        }
    }

    @Nested
    @Transactional
    @DisplayName("Тесты на метод deleteDepartment удаления отделя по id1")
    class DeleteDepartmentByIdTests {

        @Test
        @Tag("integration")
        @DisplayName("Тест с позитивным исходом")
        void deleteDepartmentByIdTest() {

            departmentService.deleteDepartment(id2);

            verify(departmentRepository, times(1)).findById(id2);
            verify(departmentRepository, times(1)).deleteById(id2);
        }

        @Test
        @Tag("integration")
        @DisplayName("Тест на выброс IdNotFoundException")
        void deleteDepartmentIdNotFoundTest() {

            IdNotFoundException exception = assertThrows(
                    IdNotFoundException.class,
                    () -> departmentService.deleteDepartment(badId)
            );

            assertEquals("Id '" + badId + "' не найден", exception.getMessage());

            verify(departmentRepository, times(1)).findById(badId);
            verify(departmentRepository, never()).deleteById(badId);
        }

        @Test
        @Tag("integration")
        @DisplayName("Тест на выброс DeleteDepartmentException")
        void deleteDepartmentByIdDeleteDepartmentExceptionTest() {

            DeleteDepartmentException exception = assertThrows(
                    DeleteDepartmentException.class,
                    () -> departmentService.deleteDepartment(id1));

            assertEquals(LEGAL_SUPPORT.getStringConvert() + " имеет сотрудников", exception.getMessage());

            verify(departmentRepository, times(1)).findById(id1);
            verify(departmentRepository, never()).deleteById(id1);
        }
    }
}
