package sadupstaff.sectionemployee;

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
import sadupstaff.dto.request.create.CreateSectionEmployeeRequest;
import sadupstaff.dto.request.update.UpdateSectionEmployeeRequest;
import sadupstaff.dto.response.SectionEmployeeResponse;
import sadupstaff.entity.district.Section;
import sadupstaff.entity.district.SectionEmployee;
import sadupstaff.enums.PositionSectionEmployeeEnum;
import sadupstaff.mapper.sectionemployee.CreateSectionEmployeeMapper;
import sadupstaff.mapper.sectionemployee.FindSectionEmployeeMapper;
import sadupstaff.mapper.sectionemployee.UpdateSectionEmployeeMapper;
import sadupstaff.repository.SectionEmployeeRepository;
import sadupstaff.service.section.SectionServiceImpl;
import sadupstaff.service.sectionemployee.SectionEmployeeServiceImpl;

import java.time.LocalDateTime;
import java.util.UUID;

@Log4j2
@SpringBootTest()
@Testcontainers
@Transactional
@DisplayName("Integration тесты методов SectionEmployeeServiceImpl")
public class SectionEmployeeServiceImplIntegrationTest {

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
    private UpdateSectionEmployeeMapper updateSectionEmployeeMapper;

    @MockitoSpyBean
    private SectionEmployeeRepository sectionEmployeeRepository;

    @MockitoSpyBean
    private SectionServiceImpl sectionService;

    @MockitoSpyBean
    private FindSectionEmployeeMapper findSectionEmployeeMapper;

    @MockitoSpyBean
    private CreateSectionEmployeeMapper createSectionEmployeeMapper;

    @Autowired
    private SectionEmployeeServiceImpl sectionEmployeeService;

    private SectionEmployee sectionEmployee;
    private CreateSectionEmployeeRequest createRequest;
    private UpdateSectionEmployeeRequest updateRequest;
    private SectionEmployeeResponse response;
    private UUID id;
    private UUID badId;

    @BeforeEach
    void setUp() {

        sectionEmployee = new SectionEmployee(
                UUID.fromString("4d30f1c3-e70d-42a0-a3d3-58a5c2d50d04"),
                "EMP12345",
                "Иван",
                "Иванов",
                "Иванович",
                PositionSectionEmployeeEnum.JUDGE,
                LocalDateTime.of(2025,07,30, 15,17,00,000),
                LocalDateTime.of(2025,07,30, 15,17,00,000),
                new Section()
        );

        id = sectionEmployee.getId();
        badId = UUID.randomUUID();

        createRequest = new CreateSectionEmployeeRequest(
                "EMP12345",
                "Иван",
                "Иванов",
                "Иванович",
                PositionSectionEmployeeEnum.JUDGE,
                "1"
        );

        response = new SectionEmployeeResponse(
                "EMP12345",
                "Иван",
                "Иванов",
                "Иванович",
                PositionSectionEmployeeEnum.JUDGE.getStringConvert(),
                "1"
        );

        updateRequest = new UpdateSectionEmployeeRequest();
    }
}
