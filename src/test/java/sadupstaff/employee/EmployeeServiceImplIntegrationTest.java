package sadupstaff.employee;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
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
import sadupstaff.dto.request.create.CreateEmployeeRequest;
import sadupstaff.dto.request.update.UpdateEmployeeRequest;
import sadupstaff.dto.response.EmployeeResponse;
import sadupstaff.entity.management.Department;
import sadupstaff.entity.management.Employee;
import sadupstaff.enums.DepartmentNameEnum;
import sadupstaff.enums.PositionEmployeeEnum;
import sadupstaff.exception.IdNotFoundException;
import sadupstaff.exception.PositionOccupiedException;
import sadupstaff.exception.employee.MaxEmployeeInDepartmentException;
import sadupstaff.mapper.employee.CreateEmployeeMapper;
import sadupstaff.mapper.employee.FindEmployeeMapper;
import sadupstaff.mapper.employee.UpdateEmployeeMapper;
import sadupstaff.repository.EmployeeRepository;
import sadupstaff.service.department.DepartmentServiceImpl;
import sadupstaff.service.employee.EmployeeServiceImpl;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static sadupstaff.enums.DepartmentNameEnum.LEGAL_SUPPORT;
import static sadupstaff.enums.PositionEmployeeEnum.*;

@Log4j2
@SpringBootTest()
@Testcontainers
@Transactional
@DisplayName("Integration тесты методов EmployeeServiceImpl")
public class EmployeeServiceImplIntegrationTest {

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
    private EmployeeRepository employeeRepository;

    @MockitoSpyBean
    private DepartmentServiceImpl departmentService;

    @MockitoSpyBean
    private UpdateEmployeeMapper updateEmployeeMapper;

    @MockitoSpyBean
    private FindEmployeeMapper findEmployeeMapper;

    @MockitoSpyBean
    private CreateEmployeeMapper createEmployeeMapper;

    @Autowired
    private EmployeeServiceImpl employeeService;

    private Employee employee;
    private CreateEmployeeRequest createRequest;
    private UpdateEmployeeRequest updateRequest;
    private UUID id;
    private UUID badId;

    @BeforeEach
    void setUp() {

        employee = new Employee(
                UUID.fromString("6d30f1c3-e70d-42a0-a3d3-58a5c2d50d04"),
                "EMP12345",
                "Иван",
                "Иванов",
                "Иванович",
                CONSULTANT,
                LocalDateTime.of(2025,07,30, 15,17,00,000),
                LocalDateTime.of(2025,07,30, 15,17,00,000),
                new Department()
        );

        id = employee.getId();
        badId = UUID.randomUUID();

        createRequest = new CreateEmployeeRequest(
                "EMP12345",
                "Иван",
                "Иванов",
                "Иванович",
                CONSULTANT,
                DepartmentNameEnum.LEGAL_SUPPORT
        );

        updateRequest = new UpdateEmployeeRequest();
    }

    @Nested
    @Transactional
    @DisplayName("Тесты на метод getAllEmployees поиска всех сотрудников")
    class GetAllEmployeeTests {

        @Test
        @Tag("integration")
        @DisplayName("Тест с позитивным исходом")
        void getAllEmployeesTest() {

            List<EmployeeResponse> result = employeeService.getAllEmployees();

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(result.get(0).getPosition(), CONSULTANT.getStringConvert());
            assertEquals(result.get(0).getDepartmentName(), LEGAL_SUPPORT.getStringConvert());

            verify(employeeRepository, times(1)).findAll();
            verify(findEmployeeMapper, times(1)).entityToResponse(any(Employee.class));
        }
    }

    @Nested
    @Transactional
    @DisplayName("Тесты на метод getEmployee поиска сотрудника по id")
    class GetEmployeeByIdTests {

        @Test
        @Tag("integration")
        @DisplayName("Тест с позитивным исходом")
        void getEmployeeByIdTest() {

            EmployeeResponse result = employeeService.getEmployee(id);

            assertNotNull(result);
            assertEquals(result.getDepartmentName(), LEGAL_SUPPORT.getStringConvert());
            assertEquals(result.getPosition(), CONSULTANT.getStringConvert());

            verify(employeeRepository, times(1)).findById(id);
            verify(findEmployeeMapper, times(1)).entityToResponse(any(Employee.class));
        }

        @Test
        @Tag("integration")
        @DisplayName("Тест с выбросом IdNotFoundException")
        void getEmployeeByIdNotFoundIdTest() {

            IdNotFoundException exception = assertThrows(IdNotFoundException.class,
                    () -> employeeService.getEmployee(badId));

            assertNotNull(exception);
            assertEquals("Id '" + badId + "' не найден", exception.getMessage());

            verify(employeeRepository, times(1)).findById(badId);
            verify(findEmployeeMapper, never()).entityToResponse(any(Employee.class));
        }
    }

    @Nested
    @Transactional
    @DisplayName("Тесты на метод saveEmployee сохранения сотрудника")
    class SaveEmployeeTests {

        @ParameterizedTest
        @EnumSource(value = PositionEmployeeEnum.class,
                    mode = EnumSource.Mode.INCLUDE,
                    names = {"HEAD_OF_DEPARTMENT",
                            "DEPUTY_HEAD_OF_DEPARTMENT",
                            "DEPARTMENT_HEAD",
                            "DEPUTY_DEPARTMENT_HEAD"})
        @Tag("integration")
        @DisplayName("Тест с позитивным исходом")
        void saveEmployeeTest(PositionEmployeeEnum position) {

            createRequest.setPosition(position);

            EmployeeResponse result = employeeService.saveEmployee(createRequest);

            assertNotNull(result);
            assertEquals(result.getPosition(), position.getStringConvert());

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
        @Tag("integration")
        @DisplayName("Тест на выброс PositionOccupiedException")
        void saveEmployeePositionOccupiedTest(PositionEmployeeEnum position) {

            createRequest.setPosition(position);

            PositionOccupiedException exception = assertThrows(
                    PositionOccupiedException.class,
                    () -> employeeService.saveEmployee(createRequest)
            );

            assertNotNull(exception);
            assertEquals("Позиция '" + position.getStringConvert() + "' уже занята", exception.getMessage());

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
        @Tag("integration")
        @DisplayName("Тест на выброс MaxEmployeeInDepartmentException")
        void saveEmployeeMaxEmployeeInDepartmentTest(PositionEmployeeEnum position) {

            createRequest.setPosition(position);

            departmentService.getDepartmentByName(LEGAL_SUPPORT).setMaxNumberEmployees(1);

            MaxEmployeeInDepartmentException exception = assertThrows(
                    MaxEmployeeInDepartmentException.class,
                    () -> employeeService.saveEmployee(createRequest)
            );

            assertNotNull(exception);
            assertEquals("В '" + LEGAL_SUPPORT.getStringConvert() + "' максимальное количество сотрудников", exception.getMessage());

            verify(createEmployeeMapper, times(1)).toEntity(any(CreateEmployeeRequest.class));
            verify(departmentService, times(2)).getDepartmentByName(createRequest.getDepartmentName());
            verify(employeeRepository, never()).findById(any(UUID.class));
            verify(employeeRepository, never()).save(any(Employee.class));
            verify(findEmployeeMapper, never()).entityToResponse(any(Employee.class));
        }
    }

    @Nested
    @Transactional
    @DisplayName("Тесты на метод updateEmployee обновления данных сотрудника")
    class UpdateEmployeeTests {

        @ParameterizedTest
        @EnumSource(value = PositionEmployeeEnum.class,
                    mode = EnumSource.Mode.EXCLUDE,
                    names = {"CONSULTANT"}
        )
        @Tag("integration")
        @DisplayName("Тест с позитивным исходом")
        void updateEmployeeTest(PositionEmployeeEnum position) {

            updateRequest.setPosition(position);

            EmployeeResponse result = employeeService.updateEmployee(id, updateRequest);

            assertNotNull(result);
            assertEquals(result.getPosition(), position.getStringConvert());

            verify(employeeRepository, times(1)).findById(any(UUID.class));
            verify(employeeRepository, times(1)).existsEmployeeByPosition(position);
            verify(updateEmployeeMapper, times(1)).updateEmployeeData(any(UpdateEmployeeRequest.class), any(Employee.class));
            verify(employeeRepository, times(1)).save(any(Employee.class));
            verify(findEmployeeMapper, times(1)).entityToResponse(any(Employee.class));
        }

        @Test
        @Tag("integration")
        @DisplayName("Тест на выброс IdNotFoundException")
        void updateEmployeeIdNotFoundTest() {

            IdNotFoundException exception = assertThrows(
                    IdNotFoundException.class,
                    () -> employeeService.updateEmployee(badId, updateRequest)
            );

            assertEquals("Id '" + badId + "' не найден", exception.getMessage());

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
        @Tag("integration")
        @DisplayName("Тест на выброс PositionOccupiedException")
        void updateEmployeePositionOccupiedTest(PositionEmployeeEnum position) {

            updateRequest.setPosition(position);

            PositionOccupiedException exception = assertThrows(
                    PositionOccupiedException.class,
                    () -> employeeService.updateEmployee(id, updateRequest)
            );

            assertEquals("Позиция '" + position.getStringConvert() + "' уже занята", exception.getMessage());

            verify(employeeRepository, times(1)).findById(any(UUID.class));
            verify(employeeRepository, times(1)).existsEmployeeByPosition(any(PositionEmployeeEnum.class));
            verify(updateEmployeeMapper, never()).updateEmployeeData(any(UpdateEmployeeRequest.class), any(Employee.class));
            verify(employeeRepository, never()).save(any(Employee.class));
            verify(findEmployeeMapper, never()).entityToResponse(any(Employee.class));
        }
    }

    @Nested
    @Transactional
    @DisplayName("Тесты на метод deleteEmployee удаления сотрудника по id")
    class DeleteEmployeeTests {

        @Test
        @Tag("integration")
        @DisplayName("Тест с позитивным исходом")
        void deleteEmployeeByIdTest() {

            employeeService.deleteEmployee(id);

            verify(employeeRepository, times(1)).findById(id);
            verify(employeeRepository, times(1)).deleteById(id);
        }

        @Test
        @Tag("integration")
        @DisplayName("Тест на выброс IdNotFoundException")
        void deleteEmployeeIdNotFoundTest() {

            IdNotFoundException exception = assertThrows(
                    IdNotFoundException.class,
                    () -> employeeService.updateEmployee(badId, updateRequest)
            );

            assertEquals("Id '" + badId + "' не найден", exception.getMessage());

            verify(employeeRepository, times(1)).findById(badId);
            verify(employeeRepository, never()).deleteById(badId);
        }
    }
}