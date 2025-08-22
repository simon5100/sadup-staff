package sadupstaff.department;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import sadupstaff.SadupStaffApplication;
import sadupstaff.dto.request.create.CreateDepartmentRequest;
import sadupstaff.dto.request.update.UpdateDepartmentRequest;
import sadupstaff.dto.response.DepartmentResponse;
import sadupstaff.entity.management.Department;
import sadupstaff.repository.DepartmentRepository;
import sadupstaff.service.department.DepartmentService;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static sadupstaff.enums.DepartmentNameEnum.LEGAL_SUPPORT;

@Log4j2
@SpringBootTest(classes = SadupStaffApplication.class)
@Testcontainers
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
    }

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DepartmentService departmentService;

    private Department department;

    private UUID id;

    private UUID badId;

    private CreateDepartmentRequest createDepartmentRequest;

    private UpdateDepartmentRequest updateDepartmentRequest;

    private DepartmentResponse response;

    @BeforeEach
    @Transactional
    void setUp(){
        department = departmentRepository.findDepartmentByName(LEGAL_SUPPORT);

        id = department.getId();
        badId = UUID.fromString("2d30f1c3-e70d-42a0-a3d3-58a5c2d50d05");

        createDepartmentRequest = new CreateDepartmentRequest(
                "FINANCE_AND_PLANNING",
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
        departmentRepository.deleteAll();
        log.info("clean db");
    }

    @Test
    @Transactional
    void saveDepartment() {

       DepartmentResponse responseCheck = departmentService.saveDepartment(createDepartmentRequest);

       assertNotNull(responseCheck);



    }
}
