package sadupstaff.section;

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
import sadupstaff.dto.request.create.CreateSectionRequest;
import sadupstaff.dto.request.update.UpdateSectionEmployeeRequest;
import sadupstaff.dto.request.update.UpdateSectionRequest;
import sadupstaff.dto.response.SectionEmployeeResponse;
import sadupstaff.dto.response.SectionResponse;
import sadupstaff.entity.district.District;
import sadupstaff.entity.district.Section;
import sadupstaff.entity.district.SectionEmployee;
import sadupstaff.enums.DistrictNameEnum;
import sadupstaff.enums.PositionSectionEmployeeEnum;
import sadupstaff.mapper.section.CreateSectionMapper;
import sadupstaff.mapper.section.FindSectionMapper;
import sadupstaff.mapper.section.UpdateSectionMapper;
import sadupstaff.mapper.sectionemployee.CreateSectionEmployeeMapper;
import sadupstaff.mapper.sectionemployee.FindSectionEmployeeMapper;
import sadupstaff.mapper.sectionemployee.UpdateSectionEmployeeMapper;
import sadupstaff.repository.SectionEmployeeRepository;
import sadupstaff.repository.SectionRepository;
import sadupstaff.service.district.DistrictServiceImpl;
import sadupstaff.service.section.SectionServiceImpl;
import sadupstaff.service.sectionemployee.SectionEmployeeServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Log4j2
@SpringBootTest()
@Testcontainers
@Transactional
@DisplayName("Integration тесты методов SectionServiceImpl")
public class SectionServiceImplIntegrationTest {

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
    private UpdateSectionMapper updateSectionMapper;

    @MockitoSpyBean
    private SectionRepository sectionRepository;

    @MockitoSpyBean
    private DistrictServiceImpl districtService;

    @MockitoSpyBean
    private FindSectionMapper findSectionMapper;

    @MockitoSpyBean
    private CreateSectionMapper createSectionMapper;

    @Autowired
    private SectionServiceImpl sectionService ;

    private Section section;
    private CreateSectionRequest createRequest;
    private UpdateSectionRequest updateRequest;
    private SectionResponse response;
    private UUID id;
    private UUID badId;

    @BeforeEach
    void setUp() {

        section = new Section(
                UUID.fromString("3d30f1c3-e70d-42a0-a3d3-58a5c2d50d04"),
                "M540000",
                "1й участок центрального района",
                3,
                LocalDateTime.of(2025,07,30, 15,17,00,000),
                LocalDateTime.of(2025,07,30, 15,17,00,000),
                new District(),
                List.of()
        );

        id = section.getId();

        badId = UUID.randomUUID();

        createRequest = new CreateSectionRequest(
                "M540000",
                "1й участок центрального района",
                3,
                DistrictNameEnum.CENTRALNY
        );

        response = new SectionResponse(
                "M540000",
                "1й участок центрального района",
                DistrictNameEnum.CENTRALNY.getStringConvert(),
                List.of()
        );

        updateRequest = new UpdateSectionRequest();
    }
}
