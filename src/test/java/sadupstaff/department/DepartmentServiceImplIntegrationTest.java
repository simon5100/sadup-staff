package sadupstaff.department;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import sadupstaff.SadupStaffApplication;
import sadupstaff.entity.management.Department;
import sadupstaff.mapper.department.CreateDepartmentMapper;
import sadupstaff.mapper.department.FindDepartmentMapper;
import sadupstaff.mapper.department.UpdateDepartmentMapper;
import sadupstaff.repository.DepartmentRepository;
import sadupstaff.service.department.DepartmentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static sadupstaff.enums.DepartmentNameEnum.LEGAL_SUPPORT;

@SpringBootTest(classes = SadupStaffApplication.class)
@Testcontainers
//@ActiveProfiles("test")
@Transactional
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
//        registry.add("spring.liquibase.change-log", () -> "classpath:/db/changelog/db.changelog-master.xml");
    }

    @BeforeEach
    void setUp(){
        System.out.println("setUp");
    }

    @AfterEach
    void tearDown(){
        System.out.println("tearDown");
    }


    @Autowired
    private DepartmentRepository departmentRepository;

    @Mock
    private UpdateDepartmentMapper updateDepartmentMapper;

    @Mock
    private FindDepartmentMapper findDepartmentMapper;

    @Mock
    private CreateDepartmentMapper createDepartmentMapper;

    @Mock
    private DepartmentService departmentService;

    @Test
    void saveDepartment() {
        departmentRepository.save(new Department(
                UUID.fromString("2d30f1c3-e70d-42a0-a3d3-58a5c2d50d04"),
                LEGAL_SUPPORT,
                5,
                "обеспечивает правами",
                LocalDateTime.now(),
                LocalDateTime.now(),
                List.of()
        ));

        // Проверяем сохранение
        assertTrue(departmentRepository.findById(UUID.fromString("2d30f1c3-e70d-42a0-a3d3-58a5c2d50d04")).isPresent());

        // Проверяем получение всех записей
        List<Department> allDepartments = departmentRepository.findAll();
        assertTrue(!allDepartments.isEmpty());
    }
}
