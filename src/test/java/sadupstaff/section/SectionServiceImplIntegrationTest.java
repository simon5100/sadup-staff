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
import sadupstaff.entity.district.SectionEmployee;
import sadupstaff.enums.DistrictNameEnum;
import sadupstaff.exception.IdNotFoundException;
import sadupstaff.exception.PositionOccupiedException;
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
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static sadupstaff.enums.DistrictNameEnum.CENTRALNY;

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

    @Nested
    @DisplayName("Тесты на метод getAllSection поиска всех районов")
    class GetAllSectionsTests {

        @Test
        @Tag("unit")
        @DisplayName("Тест с позитивным исходом")
        void getAllSectionsTest() {

            when(sectionRepository.findAll()).thenReturn(List.of(section));
            when(findSectionMapper.entityToResponse(section)).thenReturn(response);

            List<SectionResponse> result = sectionService.getAllSection();

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(response, result.get(0));

            verify(sectionRepository).findAll();
            verify(findSectionMapper, times(1)).entityToResponse(section);
        }
    }

    @Nested
    @DisplayName("Тесты на метод getDistrictById поиска района по id")
    class GetSectionByIdTests {

        @Test
        @Tag("unit")
        @DisplayName("Тест с позитивным исходом")
        void getSectionByIdTest() {

            when(sectionRepository.findById(id)).thenReturn(Optional.of(section));
            when(findSectionMapper.entityToResponse(section)).thenReturn(response);

            SectionResponse result = sectionService.getSectionById(id);

            assertNotNull(result);
            assertEquals(response, result);

            verify(sectionRepository).findById(id);
            verify(findSectionMapper, times(1)).entityToResponse(section);
        }

        @Test
        @Tag("unit")
        @DisplayName("Тест с выбросом IdNotFoundException")
        void getSectionByIdNotFoundIdTest() {

            when(sectionRepository.findById(badId)).thenReturn(Optional.empty());

            IdNotFoundException exception = assertThrows(
                    IdNotFoundException.class,
                    () -> sectionService.getSectionById(badId)
            );

            assertNotNull(exception);
            assertEquals("Id '" + badId + "' не найден", exception.getMessage());

            verify(sectionRepository).findById(badId);
            verify(findSectionMapper, never()).entityToResponse(section);
        }
    }

    @Nested
    @DisplayName("Тесты на метод getDistrictByName поиска района по имени")
    class GetSectionByNameTests {

        @ParameterizedTest
        @ValueSource(strings = {"1", "2", "3"})
        @Tag("unit")
        @DisplayName("Тест с позитивным исходом")
        void getSectionByNameTest(String name) {

            section.setName(name);

            when(sectionRepository.findSectionByName(name)).thenReturn(section);

            Section result = sectionService.getSectionByName(name);

            assertNotNull(result);
            assertEquals(section, result);

            verify(sectionRepository, times(1)).findSectionByName(name);
        }
    }

    @Nested
    @DisplayName("Тесты на метод saveSection сохранения района")
    class SaveSectionTests {

        @ParameterizedTest
        @ValueSource(strings = {"1", "2", "3"})
        @Tag("unit")
        @DisplayName("Тест с позитивным исходом")
        void saveSectionTest(String name) {

            District district = new District(
                    UUID.fromString("1d30f1c3-e70d-42a0-a3d3-58a5c2d50d04"),
                    CENTRALNY,
                    "Находится со мной в здании",
                    2,
                    LocalDateTime.of(2025,07,30, 15,17,00,000),
                    LocalDateTime.of(2025,07,30, 15,17,00,000),
                    List.of()
            );

            createRequest.setName(name);
            section.setName(name);
            response.setName(name);

            when(createSectionMapper.toEntity(createRequest)).thenReturn(section);
            when(districtService.getDistrictByName(DistrictNameEnum.CENTRALNY)).thenReturn(district);
            when(sectionRepository.save(section)).thenReturn(section);
            when(sectionRepository.findById(id)).thenReturn(Optional.of(section));
            when(findSectionMapper.entityToResponse(section)).thenReturn(response);

            SectionResponse result = sectionService.saveSection(createRequest);

            assertNotNull(result);
            assertEquals(response, result);

            verify(createSectionMapper,times(1)).toEntity(createRequest);
            verify(districtService,times(1)).getDistrictByName(DistrictNameEnum.CENTRALNY);
            verify(sectionRepository,times(1)).save(section);
            verify(sectionRepository,times(1)).findById(id);
            verify(findSectionMapper,times(1)).entityToResponse(section);
        }

        @ParameterizedTest
        @ValueSource(strings = {"1", "2", "3"})
        @Tag("unit")
        @DisplayName("Тест на выброс MaxSectionInDistrictException")
        void saveMaxSectionInDistrictTest(String name) {

            District district = new District(
                    UUID.fromString("1d30f1c3-e70d-42a0-a3d3-58a5c2d50d04"),
                    CENTRALNY,
                    "Находится со мной в здании",
                    2,
                    LocalDateTime.of(2025,07,30, 15,17,00,000),
                    LocalDateTime.of(2025,07,30, 15,17,00,000),
                    List.of(section, section)
            );
            createRequest.setName(name);
            section.setName(name);

            when(createSectionMapper.toEntity(createRequest)).thenReturn(section);
            when(districtService.getDistrictByName(DistrictNameEnum.CENTRALNY)).thenReturn(district);

            MaxSectionInDistrictException exception = assertThrows(
                    MaxSectionInDistrictException.class,
                    () -> sectionService.saveSection(createRequest)
            );

            assertNotNull(exception);
            assertEquals("В '" + district.getName().getStringConvert() + "' максимальное количество участков", exception.getMessage());

            verify(createSectionMapper,times(1)).toEntity(createRequest);
            verify(districtService,times(1)).getDistrictByName(DistrictNameEnum.CENTRALNY);
            verify(sectionRepository, never()).save(section);
            verify(sectionRepository,never()).findById(id);
            verify(findSectionMapper,never()).entityToResponse(section);
        }

        @ParameterizedTest
        @ValueSource(strings = {"1", "2", "3"})
        @Tag("unit")
        @DisplayName("Тест на выброс PositionOccupiedException")
        void saveSectionPositionOccupiedTest(String name) {

            District district = new District(
                    UUID.fromString("1d30f1c3-e70d-42a0-a3d3-58a5c2d50d04"),
                    CENTRALNY,
                    "Находится со мной в здании",
                    2,
                    LocalDateTime.of(2025,07,30, 15,17,00,000),
                    LocalDateTime.of(2025,07,30, 15,17,00,000),
                    List.of(section)
            );
            createRequest.setName(name);
            section.setName(name);

            when(createSectionMapper.toEntity(createRequest)).thenReturn(section);
            when(districtService.getDistrictByName(DistrictNameEnum.CENTRALNY)).thenReturn(district);

            PositionOccupiedException exception = assertThrows(
                    PositionOccupiedException.class,
                    () -> sectionService.saveSection(createRequest)
            );

            assertNotNull(exception);
            assertEquals("Позиция '" + name + "' уже занята", exception.getMessage());

            verify(createSectionMapper,times(1)).toEntity(createRequest);
            verify(districtService,times(1)).getDistrictByName(DistrictNameEnum.CENTRALNY);
            verify(sectionRepository, never()).save(section);
            verify(sectionRepository,never()).findById(id);
            verify(findSectionMapper,never()).entityToResponse(section);
        }
    }

    @Nested
    @DisplayName("Тесты на метод updateSection обновления данных района")
    class UpdateSectionTests {

        @ParameterizedTest
        @ValueSource(strings = {"1", "2", "3"})
        @Tag("unit")
        @DisplayName("Тест с позитивным исходом")
        void updateSectionTest(String name) {

            updateRequest.setName(name);
            response.setName(name);

            when(sectionRepository.findById(id)).thenReturn(Optional.of(section));
            when(sectionRepository.existsSectionByName(updateRequest.getName())).thenReturn(false);
            doAnswer(invocationOnMock -> {
                section.setName(updateRequest.getName());
                return null;
            }).when(updateSectionMapper).update(updateRequest, section);
            when(sectionRepository.save(section)).thenReturn(section);
            when(sectionRepository.findById(id)).thenReturn(Optional.of(section));
            when(findSectionMapper.entityToResponse(section)).thenReturn(response);

            SectionResponse result = sectionService.updateSection(id, updateRequest);

            assertNotNull(result);
            assertEquals(response, result);

            verify(sectionRepository, times(1)).findById(id);
            verify(sectionRepository, times(1)).existsSectionByName(updateRequest.getName());
            verify(updateSectionMapper, times(1)).update(updateRequest, section);
            verify(sectionRepository, times(1)).save(section);
            verify(findSectionMapper, times(1)).entityToResponse(section);

        }

        @Test
        @Tag("unit")
        @DisplayName("Тест на выброс IdNotFoundException")
        void updateSectionIdNotFoundTest() {
            updateRequest.setName("1");

            when(sectionRepository.findById(badId)).thenReturn(Optional.empty());

            IdNotFoundException exception = assertThrows(
                    IdNotFoundException.class,
                    () -> sectionService.updateSection(badId, updateRequest)
            );

            assertNotNull(exception);
            assertEquals("Id '" + badId + "' не найден", exception.getMessage());

            verify(sectionRepository, times(1)).findById(badId);
            verify(sectionRepository, never()).existsSectionByName(updateRequest.getName());
            verify(updateSectionMapper, never()).update(updateRequest, section);
            verify(sectionRepository, never()).save(section);
            verify(findSectionMapper, never()).entityToResponse(section);
        }

        @ParameterizedTest
        @ValueSource(strings = {"1", "2", "3"})
        @Tag("unit")
        @DisplayName("Тест на выброс PositionOccupiedException")
        void updateSectionPositionOccupiedTest(String name) {

            updateRequest.setName(name);

            when(sectionRepository.findById(id)).thenReturn(Optional.of(section));
            when(sectionRepository.existsSectionByName(updateRequest.getName())).thenReturn(true);

            PositionOccupiedException exception = assertThrows(
                    PositionOccupiedException.class,
                    () -> sectionService.updateSection(id, updateRequest)
            );

            assertNotNull(exception);
            assertEquals("Позиция '" + name + "' уже занята", exception.getMessage());

            verify(sectionRepository, times(1)).findById(id);
            verify(sectionRepository, times(1)).existsSectionByName(updateRequest.getName());
            verify(updateSectionMapper, never()).update(updateRequest, section);
            verify(sectionRepository, never()).save(section);
            verify(findSectionMapper, never()).entityToResponse(section);
        }
    }

    @Nested
    @DisplayName("Тесты на метод deleteSection удаления района по id")
    class DeleteSectionByIdTests {

        @Test
        @Tag("unit")
        @DisplayName("Тест с позитивным исходом")
        void deleteSectionByIdTest() {

            when(sectionRepository.findById(id)).thenReturn(Optional.of(section));

            sectionService.deleteSection(id);

            verify(sectionRepository, times(1)).findById(id);
        }

        @Test
        @Tag("unit")
        @DisplayName("Тест на выброс IdNotFoundException")
        void deleteSectionIdNotFoundTest() {

            when(sectionRepository.findById(badId)).thenReturn(Optional.empty());

            IdNotFoundException exception = assertThrows(
                    IdNotFoundException.class,
                    () -> sectionService.deleteSection(badId)
            );

            assertNotNull(exception);
            assertEquals("Id '" + badId + "' не найден", exception.getMessage());

            verify(sectionRepository, times(1)).findById(badId);
        }

        @Test
        @Tag("unit")
        @DisplayName("Тест на выброс DeleteSectionException")
        void deleteSectionByIdDeleteSectionExceptionTest() {

            section.setEmpsSect(List.of(new SectionEmployee()));

            when(sectionRepository.findById(id)).thenReturn(Optional.of(section));

            DeleteSectionException exception = assertThrows(
                    DeleteSectionException.class,
                    () -> sectionService.deleteSection(id)
            );

            assertNotNull(exception);
            assertFalse(section.getEmpsSect().isEmpty());
            assertEquals(section.getName() + " имеет сотрудников, удаление запрещено", exception.getMessage());

            verify(sectionRepository, times(1)).findById(id);
        }
    }






}
