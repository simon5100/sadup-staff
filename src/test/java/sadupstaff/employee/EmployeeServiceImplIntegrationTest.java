package sadupstaff.employee;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static sadupstaff.enums.DepartmentNameEnum.LEGAL_SUPPORT;
import static sadupstaff.enums.PositionEmployeeEnum.CONSULTANT;

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
    private EmployeeResponse response;
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

        response = new EmployeeResponse(
                "EMP12345",
                "Иван",
                "Иванов",
                "Иванович",
                CONSULTANT.getStringConvert(),
                DepartmentNameEnum.LEGAL_SUPPORT.getStringConvert(),
                LocalDateTime.of(2025,07,30, 15,17,00,000),
                LocalDateTime.of(2025,07,30, 15,17,00,000)
        );

        updateRequest = new UpdateEmployeeRequest();
    }

    @Nested
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
    @DisplayName("Тесты на метод getEmployee поиска сотрудника по id")
    class GetEmployeeByIdTests {

        @Test
        @Tag("integration")
        @DisplayName("Тест с позитивным исходом")
        void getEmployeeByIdTest() {

            when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));
            when(findEmployeeMapper.entityToResponse(employee)).thenReturn(response);

            EmployeeResponse result = employeeService.getEmployee(id);

            assertNotNull(result);
            assertEquals(response, result);

            verify(employeeRepository, times(1)).findById(id);
            verify(findEmployeeMapper, times(1)).entityToResponse(employee);
        }

        @Test
        @Tag("integration")
        @DisplayName("Тест с выбросом IdNotFoundException")
        void getEmployeeByIdNotFoundIdTest() {

            when(employeeRepository.findById(badId)).thenReturn(Optional.empty());

            IdNotFoundException exception = assertThrows(IdNotFoundException.class,
                    () -> employeeService.getEmployee(badId));

            assertNotNull(exception);
            assertEquals("Id '" + badId + "' не найден", exception.getMessage());

            verify(employeeRepository, times(1)).findById(badId);
            verify(findEmployeeMapper, never()).entityToResponse(employee);
        }
    }

    @Nested
    @DisplayName("Тесты на метод saveEmployee сохранения сотрудника")
    class SaveEmployeeTests {

        @ParameterizedTest
        @EnumSource(PositionEmployeeEnum.class)
        @Tag("integration")
        @DisplayName("Тест с позитивным исходом")
        void saveEmployeeTest(PositionEmployeeEnum position) {
            Department department = new Department(
                    UUID.fromString("2d30f1c3-e70d-42a0-a3d3-58a5c2d50d04"),
                    LEGAL_SUPPORT,
                    5,
                    "обеспечивает правами",
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    List.of()
            );

            employee.setPosition(position);
            createRequest.setPosition(position);
            response.setPosition(position.getStringConvert());

            when(createEmployeeMapper.toEntity(createRequest)).thenReturn(employee);
            when(departmentService.getDepartmentByName(createRequest.getDepartmentName())).thenReturn(department);
            when(employeeRepository.save(employee)).thenReturn(employee);
            when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));
            when(findEmployeeMapper.entityToResponse(employee)).thenReturn(response);

            EmployeeResponse result = employeeService.saveEmployee(createRequest);

            assertNotNull(result);
            assertEquals(response, result);

            verify(createEmployeeMapper, times(1)).toEntity(createRequest);
            verify(departmentService, times(1)).getDepartmentByName(createRequest.getDepartmentName());
            verify(employeeRepository, times(1)).findById(id);
            verify(employeeRepository, times(1)).save(employee);
            verify(findEmployeeMapper, times(1)).entityToResponse(employee);
        }

        @ParameterizedTest
        @EnumSource(PositionEmployeeEnum.class)
        @Tag("integration")
        @DisplayName("Тест на выброс PositionOccupiedException")
        void saveEmployeePositionOccupiedTest(PositionEmployeeEnum position) {

            employee.setPosition(position);
            createRequest.setPosition(position);

            Department department = new Department(
                    UUID.fromString("2d30f1c3-e70d-42a0-a3d3-58a5c2d50d04"),
                    LEGAL_SUPPORT,
                    5,
                    "обеспечивает правами",
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    List.of(employee)
            );

            when(createEmployeeMapper.toEntity(createRequest)).thenReturn(employee);
            when(departmentService.getDepartmentByName(createRequest.getDepartmentName())).thenReturn(department);

            PositionOccupiedException exception = assertThrows(
                    PositionOccupiedException.class,
                    () -> employeeService.saveEmployee(createRequest)
            );

            assertNotNull(exception);
            assertEquals("Позиция '" + position.getStringConvert() + "' уже занята", exception.getMessage());

            verify(createEmployeeMapper, times(1)).toEntity(createRequest);
            verify(departmentService, times(1)).getDepartmentByName(createRequest.getDepartmentName());
            verify(employeeRepository, never()).findById(id);
            verify(employeeRepository, never()).save(employee);
            verify(findEmployeeMapper, never()).entityToResponse(employee);
        }

        @ParameterizedTest
        @EnumSource(PositionEmployeeEnum.class)
        @Tag("integration")
        @DisplayName("Тест на выброс MaxEmployeeInDepartmentException")
        void saveEmployeeMaxEmployeeInDepartmentTest(PositionEmployeeEnum position) {

            employee.setPosition(position);
            createRequest.setPosition(position);

            Department department = new Department(
                    UUID.fromString("2d30f1c3-e70d-42a0-a3d3-58a5c2d50d04"),
                    LEGAL_SUPPORT,
                    1,
                    "обеспечивает правами",
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    List.of(employee)
            );

            when(createEmployeeMapper.toEntity(createRequest)).thenReturn(employee);
            when(departmentService.getDepartmentByName(createRequest.getDepartmentName())).thenReturn(department);

            MaxEmployeeInDepartmentException exception = assertThrows(
                    MaxEmployeeInDepartmentException.class,
                    () -> employeeService.saveEmployee(createRequest)
            );

            assertNotNull(exception);
            assertEquals("В '" + department.getName().getStringConvert() + "' максимальное количество сотрудников", exception.getMessage());

            verify(createEmployeeMapper, times(1)).toEntity(createRequest);
            verify(departmentService, times(1)).getDepartmentByName(createRequest.getDepartmentName());
            verify(employeeRepository, never()).findById(id);
            verify(employeeRepository, never()).save(employee);
            verify(findEmployeeMapper, never()).entityToResponse(employee);
        }
    }

    @Nested
    @DisplayName("Тесты на метод updateEmployee обновления данных сотрудника")
    class UpdateEmployeeTests {

        @ParameterizedTest
        @EnumSource(PositionEmployeeEnum.class)
        @Tag("integration")
        @DisplayName("Тест с позитивным исходом")
        void updateEmployeeTest(PositionEmployeeEnum position) {

            updateRequest.setPosition(position);
            response.setPosition(position.getStringConvert());

            when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));
            when(employeeRepository.existsEmployeeByPosition(position)).thenReturn(false);
            doAnswer(invocation -> {
                employee.setPosition(position);
                return null;
            }).when(updateEmployeeMapper).updateEmployeeData(updateRequest, employee);
            when(employeeRepository.save(employee)).thenReturn(employee);
            when(findEmployeeMapper.entityToResponse(employee)).thenReturn(response);

            EmployeeResponse result = employeeService.updateEmployee(id, updateRequest);

            assertNotNull(result);
            assertEquals(response, result);

            verify(employeeRepository, times(1)).findById(id);
            verify(employeeRepository, times(1)).existsEmployeeByPosition(position);
            verify(updateEmployeeMapper, times(1)).updateEmployeeData(updateRequest, employee);
            verify(employeeRepository, times(1)).save(employee);
            verify(findEmployeeMapper, times(1)).entityToResponse(employee);
        }

        @Test
        @Tag("integration")
        @DisplayName("Тест на выброс IdNotFoundException")
        void updateEmployeeIdNotFoundTest() {

            when(employeeRepository.findById(badId)).thenReturn(Optional.empty());

            IdNotFoundException exception = assertThrows(
                    IdNotFoundException.class,
                    () -> employeeService.updateEmployee(badId, updateRequest)
            );

            assertEquals("Id '" + badId + "' не найден", exception.getMessage());

            verify(employeeRepository, times(1)).findById(badId);
            verify(employeeRepository, never()).existsEmployeeByPosition(any(PositionEmployeeEnum.class));
            verify(updateEmployeeMapper, never()).updateEmployeeData(updateRequest, employee);
            verify(employeeRepository, never()).save(employee);
            verify(findEmployeeMapper, never()).entityToResponse(employee);
        }

        @ParameterizedTest
        @EnumSource(PositionEmployeeEnum.class)
        @Tag("integration")
        @DisplayName("Тест на выброс PositionOccupiedException")
        void updateEmployeePositionOccupiedTest(PositionEmployeeEnum position) {

            updateRequest.setPosition(position);

            when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));
            when(employeeRepository.existsEmployeeByPosition(position)).thenReturn(true);

            PositionOccupiedException exception = assertThrows(
                    PositionOccupiedException.class,
                    () -> employeeService.updateEmployee(id, updateRequest)
            );

            assertEquals("Позиция '" + position.getStringConvert() + "' уже занята", exception.getMessage());

            verify(employeeRepository, times(1)).findById(id);
            verify(employeeRepository, times(1)).existsEmployeeByPosition(any(PositionEmployeeEnum.class));
            verify(updateEmployeeMapper, never()).updateEmployeeData(updateRequest, employee);
            verify(employeeRepository, never()).save(employee);
            verify(findEmployeeMapper, never()).entityToResponse(employee);
        }
    }

    @Nested
    @DisplayName("Тесты на метод deleteEmployee удаления сотрудника по id")
    class DeleteEmployeeTests {

        @Test
        @Tag("integration")
        @DisplayName("Тест с позитивным исходом")
        void deleteEmployeeByIdTest() {

            when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));
            doAnswer(invocation -> {
                return null;
            }).when(employeeRepository).deleteById(id);

            employeeService.deleteEmployee(id);

            verify(employeeRepository, times(1)).findById(id);
            verify(employeeRepository, times(1)).deleteById(id);
        }

        @Test
        @Tag("integration")
        @DisplayName("Тест на выброс IdNotFoundException")
        void deleteEmployeeIdNotFoundTest() {

            when(employeeRepository.findById(badId)).thenReturn(Optional.empty());

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
