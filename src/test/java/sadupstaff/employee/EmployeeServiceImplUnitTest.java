package sadupstaff.employee;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sadupstaff.dto.request.create.CreateEmployeeRequest;
import sadupstaff.dto.request.update.UpdateEmployeeRequest;
import sadupstaff.dto.response.DistrictResponse;
import sadupstaff.dto.response.EmployeeResponse;
import sadupstaff.entity.district.District;
import sadupstaff.entity.district.Section;
import sadupstaff.entity.management.Department;
import sadupstaff.entity.management.Employee;
import sadupstaff.enums.DepartmentNameEnum;
import sadupstaff.enums.DistrictNameEnum;
import sadupstaff.enums.PositionEmployeeEnum;
import sadupstaff.exception.IdNotFoundException;
import sadupstaff.exception.PositionOccupiedException;
import sadupstaff.exception.district.DeleteDistrictException;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;
import static sadupstaff.enums.DepartmentNameEnum.LEGAL_SUPPORT;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit тесты методов EmployeeServiceImpl")
public class EmployeeServiceImplUnitTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentServiceImpl departmentService;

    @Mock
    private UpdateEmployeeMapper updateEmployeeMapper;

    @Mock
    private FindEmployeeMapper findEmployeeMapper;

    @Mock
    private CreateEmployeeMapper createEmployeeMapper;

    @InjectMocks
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
                PositionEmployeeEnum.CONSULTANT,
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
                PositionEmployeeEnum.CONSULTANT,
                DepartmentNameEnum.LEGAL_SUPPORT
        );

        response = new EmployeeResponse(
                "EMP12345",
                "Иван",
                "Иванов",
                "Иванович",
                PositionEmployeeEnum.CONSULTANT.getStringConvert(),
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
        @Tag("unit")
        @DisplayName("Тест с позитивным исходом")
        void getAllEmployeesTest() {

            when(employeeRepository.findAll()).thenReturn(List.of(employee));
            when(findEmployeeMapper.entityToResponse(employee)).thenReturn(response);

            List<EmployeeResponse> result = employeeService.getAllEmployees();

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(response, result.get(0));

            verify(employeeRepository, times(1)).findAll();
            verify(findEmployeeMapper, times(1)).entityToResponse(employee);
        }
    }

    @Nested
    @DisplayName("Тесты на метод getEmployee поиска сотрудника по id")
    class GetEmployeeByIdTests {

        @Test
        @Tag("unit")
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
        @Tag("unit")
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
        @Tag("unit")
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
        @Tag("unit")
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
    }

    @Nested
    @DisplayName("Тесты на метод updateEmployee обновления данных сотрудника")
    class UpdateEmployeeTests {

        @ParameterizedTest
        @EnumSource(DistrictNameEnum.class)
        @Tag("unit")
        @DisplayName("Тест с позитивным исходом")
        void updateEmployeeTest(DistrictNameEnum name) {


        }

        @Test
        @Tag("unit")
        @DisplayName("Тест на выброс IdNotFoundException")
        void updateEmployeeIdNotFoundTest() {



        }

        @ParameterizedTest
        @EnumSource(DistrictNameEnum.class)
        @Tag("unit")
        @DisplayName("Тест на выброс PositionOccupiedException")
        void updateEmployeePositionOccupiedTest(DistrictNameEnum name) {


        }
    }

    @Nested
    @DisplayName("Тесты на метод deleteEmployee удаления сотрудника по id")
    class DeleteEmployeeTests {

        @Test
        @Tag("unit")
        @DisplayName("Тест с позитивным исходом")
        void deleteEmployeeByIdTest() {


        }

        @Test
        @Tag("unit")
        @DisplayName("Тест на выброс IdNotFoundException")
        void deleteEmployeeIdNotFoundTest() {


        }
    }
}
