package sadupstaff.section;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import sadupstaff.dto.request.create.CreateSectionRequest;
import sadupstaff.dto.request.update.UpdateSectionRequest;
import sadupstaff.dto.response.SectionResponse;
import sadupstaff.entity.district.District;
import sadupstaff.entity.district.Section;
import sadupstaff.enums.DistrictName;
import sadupstaff.exception.IdNotFoundException;
import sadupstaff.exception.PositionOccupiedException;
import sadupstaff.exception.SectionNotFoundByPersonelNumberException;
import sadupstaff.exception.section.DeleteSectionException;
import sadupstaff.exception.section.MaxSectionInDistrictException;
import sadupstaff.mapper.section.CreateSectionMapper;
import sadupstaff.mapper.section.FindSectionMapper;
import sadupstaff.mapper.section.UpdateSectionMapper;
import sadupstaff.repository.SectionRepository;
import sadupstaff.service.district.DistrictServiceImpl;
import sadupstaff.service.section.SectionServiceImpl;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static sadupstaff.enums.DistrictName.ZHELEZNODOROZHHNY;

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
    private UUID id;
    private UUID badId;

    @BeforeEach
    void setUp() {

        section = new Section(
                UUID.fromString("3d30f1c3-e70d-42a0-a3d3-58a5c2d50d04"),
                "54MS0068",
                2,
                3,
                LocalDateTime.of(2025,07,30, 15,17,00,000),
                LocalDateTime.of(2025,07,30, 15,17,00,000),
                new District(),
                List.of()
        );

        id = section.getId();

        badId = UUID.randomUUID();

        createRequest = new CreateSectionRequest(
                "54MS0000",
                1,
                3,
                DistrictName.CENTRALNY
        );

        updateRequest = new UpdateSectionRequest();
    }

    @Nested
    @Testcontainers
    @DisplayName("Тесты на метод getAllSection поиска всех районов")
    class GetAllSectionsTests {

        @Test
        @Tag("integration")
        @DisplayName("Тест с позитивным исходом")
        void getAllSectionsTest() {

            List<SectionResponse> result = sectionService.getAllSection();

            assertNotNull(result);
            assertEquals(3, result.size());
            assertEquals(result.get(0).getNumber(), 2);

            verify(sectionRepository).findAll();
            verify(findSectionMapper, times(3)).entityToResponse(any(Section.class));
        }
    }

    @Nested
    @Testcontainers
    @DisplayName("Тесты на метод getSectionById поиска района по id")
    class GetSectionByIdTests {

        @Test
        @Tag("integration")
        @DisplayName("Тест с позитивным исходом")
        void getSectionByIdTest() {

            SectionResponse result = sectionService.getSectionById(id);

            assertNotNull(result);
            assertEquals(result.getNumber(), 2);
            assertFalse(result.getEmpsSect().isEmpty());

            verify(sectionRepository).findById(id);
            verify(findSectionMapper, times(1)).entityToResponse(any(Section.class));
        }

        @Test
        @Tag("integration")
        @DisplayName("Тест с выбросом IdNotFoundException")
        void getSectionByIdNotFoundIdTest() {

            IdNotFoundException exception = assertThrows(
                    IdNotFoundException.class,
                    () -> sectionService.getSectionById(badId)
            );

            assertNotNull(exception);
            assertEquals("Id '" + badId + "' не найден", exception.getMessage());

            verify(sectionRepository).findById(badId);
            verify(findSectionMapper, never()).entityToResponse(any(Section.class));
        }
    }

    @Nested
    @Testcontainers
    @DisplayName("Тесты на метод getDistrictByName поиска района по имени")
    class GetSectionByNameTests {

        @Test
        @Tag("integration")
        @DisplayName("Тест с позитивным исходом")
        void getSectionByPersonelNumberTest() {

            Section result = sectionService.getSectionByPersonelNumber("54MS0010");

            assertNotNull(result);
            assertEquals(result.getNumber(), 2);

            verify(sectionRepository, times(1)).findSectionByPersonelNumber("54MS0010");
        }

        @Test
        @Tag("integration")
        @DisplayName("Тест с выбросом SectionNotFoundByPersonelNumberException")
        void getSectionByPersonelNumberNotFoundTest() {

            SectionNotFoundByPersonelNumberException exception = assertThrows(SectionNotFoundByPersonelNumberException.class,
                    () -> sectionService.getSectionByPersonelNumber("54MS00000"));

            assertNotNull(exception);
            assertEquals(exception.getMessage(), "Участок '54MS00000' не найден");

            verify(sectionRepository, times(1)).findSectionByPersonelNumber("54MS00000");
        }
    }

    @Nested
    @Testcontainers
    @DisplayName("Тесты на метод saveSection сохранения района")
    class SaveSectionTests {

        @ParameterizedTest
        @ValueSource(strings = {"54MS0002"})
        @Tag("integration")
        @DisplayName("Тест с позитивным исходом")
        void saveSectionTest(String number) {

            createRequest.setPersonelNumber(number);
            createRequest.setDistrictName(ZHELEZNODOROZHHNY);

            SectionResponse result = sectionService.saveSection(createRequest);

            assertNotNull(result);
            assertEquals(result.getPersonelNumber(), number);

            verify(createSectionMapper,times(1)).toEntity(any(CreateSectionRequest.class));
            verify(districtService,times(1)).getDistrictByName(any(DistrictName.class));
            verify(sectionRepository,times(1)).save(any(Section.class));
            verify(sectionRepository,times(1)).findById(any(UUID.class));
            verify(findSectionMapper,times(1)).entityToResponse(any(Section.class));
        }

        @Test
        @Tag("integration")
        @DisplayName("Тест на выброс MaxSectionInDistrictException")
        void saveMaxSectionInDistrictTest() {

            createRequest.setPersonelNumber("54MS0004");

            MaxSectionInDistrictException exception = assertThrows(
                    MaxSectionInDistrictException.class,
                    () -> sectionService.saveSection(createRequest)
            );

            assertNotNull(exception);
            assertEquals("В '" + createRequest.getDistrictName().getStringConvert() + "' максимальное количество участков", exception.getMessage());

            verify(createSectionMapper,times(1)).toEntity(any(CreateSectionRequest.class));
            verify(districtService,times(1)).getDistrictByName(any(DistrictName.class));
            verify(sectionRepository, never()).save(any(Section.class));
            verify(sectionRepository,never()).findById(any(UUID.class));
            verify(findSectionMapper,never()).entityToResponse(any(Section.class));
        }

        @Test
        @Tag("integration")
        @DisplayName("Тест на выброс PositionOccupiedException")
        void saveSectionPositionOccupiedTest() {

            createRequest.setDistrictName(ZHELEZNODOROZHHNY);
            createRequest.setPersonelNumber("54MS0010");

            PositionOccupiedException exception = assertThrows(
                    PositionOccupiedException.class,
                    () -> sectionService.saveSection(createRequest)
            );

            assertNotNull(exception);
            assertEquals("Позиция '" + createRequest.getPersonelNumber() + "' уже занята", exception.getMessage());

            verify(createSectionMapper,times(1)).toEntity(any(CreateSectionRequest.class));
            verify(districtService,times(1)).getDistrictByName(any(DistrictName.class));
            verify(sectionRepository, never()).save(any(Section.class));
            verify(sectionRepository,never()).findById(any(UUID.class));
            verify(findSectionMapper,never()).entityToResponse(any(Section.class));
        }
    }

    @Nested
    @Testcontainers
    @DisplayName("Тесты на метод updateSection обновления данных района")
    class UpdateSectionTests {

        @ParameterizedTest
        @ValueSource(strings = {"54MS0004", "54MS0003"})
        @Tag("integration")
        @DisplayName("Тест с позитивным исходом")
        void updateSectionTest(String number) {

            updateRequest.setPersonelNumber(number);

            SectionResponse result = sectionService.updateSection(id, updateRequest);

            assertNotNull(result);
            assertEquals(result.getPersonelNumber(), number);

            verify(sectionRepository, times(1)).findById(any(UUID.class));
            verify(sectionRepository, times(1)).existsSectionByPersonelNumber(updateRequest.getPersonelNumber());
            verify(updateSectionMapper, times(1)).update(any(UpdateSectionRequest.class), any(Section.class));
            verify(sectionRepository, times(1)).save(any(Section.class));
            verify(findSectionMapper, times(1)).entityToResponse(any(Section.class));

        }

        @Test
        @Tag("integration")
        @DisplayName("Тест на выброс IdNotFoundException")
        void updateSectionIdNotFoundTest() {

            IdNotFoundException exception = assertThrows(
                    IdNotFoundException.class,
                    () -> sectionService.updateSection(badId, updateRequest)
            );

            assertNotNull(exception);
            assertEquals("Id '" + badId + "' не найден", exception.getMessage());

            verify(sectionRepository, times(1)).findById(badId);
            verify(sectionRepository, never()).existsSectionByPersonelNumber(updateRequest.getPersonelNumber());
            verify(updateSectionMapper, never()).update(updateRequest, section);
            verify(sectionRepository, never()).save(section);
            verify(findSectionMapper, never()).entityToResponse(section);
        }

        @ParameterizedTest
        @ValueSource(strings = {"54MS0068", "54MS0010"})
        @Tag("integration")
        @DisplayName("Тест на выброс PositionOccupiedException")
        void updateSectionPositionOccupiedTest(String number) {

            updateRequest.setPersonelNumber(number);

            PositionOccupiedException exception = assertThrows(
                    PositionOccupiedException.class,
                    () -> sectionService.updateSection(id, updateRequest)
            );

            assertNotNull(exception);
            assertEquals("Позиция '" + number + "' уже занята", exception.getMessage());

            verify(sectionRepository, times(1)).findById(id);
            verify(sectionRepository, times(1)).existsSectionByPersonelNumber(updateRequest.getPersonelNumber());
            verify(updateSectionMapper, never()).update(updateRequest, section);
            verify(sectionRepository, never()).save(section);
            verify(findSectionMapper, never()).entityToResponse(section);
        }
    }

    @Nested
    @Testcontainers
    @DisplayName("Тесты на метод deleteSection удаления района по id")
    class DeleteSectionByIdTests {

        @Test
        @Tag("integration")
        @DisplayName("Тест с позитивным исходом")
        void deleteSectionByIdTest() {

            sectionService.deleteSection(UUID.fromString("5d30f1c3-e70d-42a0-a3d3-58a5c2d50d04"));

            verify(sectionRepository, times(1)).findById(UUID.fromString("5d30f1c3-e70d-42a0-a3d3-58a5c2d50d04"));
        }

        @Test
        @Tag("integration")
        @DisplayName("Тест на выброс IdNotFoundException")
        void deleteSectionIdNotFoundTest() {

            IdNotFoundException exception = assertThrows(
                    IdNotFoundException.class,
                    () -> sectionService.deleteSection(badId)
            );

            assertNotNull(exception);
            assertEquals("Id '" + badId + "' не найден", exception.getMessage());

            verify(sectionRepository, times(1)).findById(badId);
        }

        @Test
        @Tag("integration")
        @DisplayName("Тест на выброс DeleteSectionException")
        void deleteSectionByIdDeleteSectionExceptionTest() {

            DeleteSectionException exception = assertThrows(
                    DeleteSectionException.class,
                    () -> sectionService.deleteSection(id)
            );

            assertNotNull(exception);
            assertEquals(section.getNumber() + " имеет сотрудников, удаление запрещено", exception.getMessage());

            verify(sectionRepository, times(1)).findById(id);
        }
    }
}
