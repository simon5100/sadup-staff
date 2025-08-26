package sadupstaff.employee;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import sadupstaff.mapper.employee.CreateEmployeeMapper;
import sadupstaff.mapper.employee.FindEmployeeMapper;
import sadupstaff.mapper.employee.UpdateEmployeeMapper;
import sadupstaff.repository.EmployeeRepository;
import sadupstaff.service.department.DepartmentServiceImpl;
import sadupstaff.service.employee.EmployeeServiceImpl;

import java.time.LocalDateTime;
import java.util.UUID;

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
}
