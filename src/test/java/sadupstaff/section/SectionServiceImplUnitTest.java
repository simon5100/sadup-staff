package sadupstaff.section;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sadupstaff.dto.request.create.CreateSectionRequest;
import sadupstaff.dto.request.update.UpdateSectionRequest;
import sadupstaff.dto.response.SectionResponse;
import sadupstaff.entity.district.District;
import sadupstaff.entity.district.Section;
import sadupstaff.entity.district.SectionEmployee;
import sadupstaff.enums.DistrictName;
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
import static org.mockito.Mockito.*;
import static sadupstaff.enums.DistrictName.CENTRALNY;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit тесты методов SectionServiceImpl")
public class SectionServiceImplUnitTest {

    @Mock
    private UpdateSectionMapper updateSectionMapper;

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private DistrictServiceImpl districtService;

    @Mock
    private FindSectionMapper findSectionMapper;

    @Mock
    private CreateSectionMapper createSectionMapper;

    @InjectMocks
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
                1,
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
                1,
                3,
                DistrictName.CENTRALNY
        );

        response = new SectionResponse(
                "M540000",
                1,
                DistrictName.CENTRALNY.getStringConvert(),
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
    class GetSectionByNumberTests {

        @ParameterizedTest
        @ValueSource(strings = {"1","2", "3"})
        @Tag("unit")
        @DisplayName("Тест с позитивным исходом")
        void getSectionByNameTest(String number) {

            section.setPersonelNumber(number);

            when(sectionRepository.findSectionByPersonelNumber(number)).thenReturn(section);

            Section result = sectionService.getSectionByPersonelNumber(number);

            assertNotNull(result);
            assertEquals(section, result);

            verify(sectionRepository, times(1)).findSectionByPersonelNumber(number);
        }
    }

    @Nested
    @DisplayName("Тесты на метод saveSection сохранения района")
    class SaveSectionTests {

        @ParameterizedTest
        @ValueSource(strings = {"1","2", "3"})
        @Tag("unit")
        @DisplayName("Тест с позитивным исходом")
        void saveSectionTest(String number) {

            District district = new District(
                    UUID.fromString("1d30f1c3-e70d-42a0-a3d3-58a5c2d50d04"),
                    CENTRALNY,
                    "Находится со мной в здании",
                    2,
                    LocalDateTime.of(2025,07,30, 15,17,00,000),
                    LocalDateTime.of(2025,07,30, 15,17,00,000),
                    List.of()
            );

            createRequest.setPersonelNumber(number);
            section.setPersonelNumber(number);
            response.setPersonelNumber(number);

            when(createSectionMapper.toEntity(createRequest)).thenReturn(section);
            when(districtService.getDistrictByName(DistrictName.CENTRALNY)).thenReturn(district);
            when(sectionRepository.save(section)).thenReturn(section);
            when(sectionRepository.findById(id)).thenReturn(Optional.of(section));
            when(findSectionMapper.entityToResponse(section)).thenReturn(response);

            SectionResponse result = sectionService.saveSection(createRequest);

            assertNotNull(result);
            assertEquals(response, result);

            verify(createSectionMapper,times(1)).toEntity(createRequest);
            verify(districtService,times(1)).getDistrictByName(DistrictName.CENTRALNY);
            verify(sectionRepository,times(1)).save(section);
            verify(sectionRepository,times(1)).findById(id);
            verify(findSectionMapper,times(1)).entityToResponse(section);
        }

        @ParameterizedTest
        @ValueSource(strings = {"1","2", "3"})
        @Tag("unit")
        @DisplayName("Тест на выброс MaxSectionInDistrictException")
        void saveMaxSectionInDistrictTest(String number) {

            District district = new District(
                    UUID.fromString("1d30f1c3-e70d-42a0-a3d3-58a5c2d50d04"),
                    CENTRALNY,
                    "Находится со мной в здании",
                    2,
                    LocalDateTime.of(2025,07,30, 15,17,00,000),
                    LocalDateTime.of(2025,07,30, 15,17,00,000),
                    List.of(section, section)
            );
            createRequest.setPersonelNumber(number);
            section.setPersonelNumber(number);

            when(createSectionMapper.toEntity(createRequest)).thenReturn(section);
            when(districtService.getDistrictByName(CENTRALNY)).thenReturn(district);

            MaxSectionInDistrictException exception = assertThrows(
                    MaxSectionInDistrictException.class,
                    () -> sectionService.saveSection(createRequest)
            );

            assertNotNull(exception);
            assertEquals("В '" + district.getName().getStringConvert() + "' максимальное количество участков", exception.getMessage());

            verify(createSectionMapper,times(1)).toEntity(createRequest);
            verify(districtService,times(1)).getDistrictByName(CENTRALNY);
            verify(sectionRepository, never()).save(section);
            verify(sectionRepository,never()).findById(id);
            verify(findSectionMapper,never()).entityToResponse(section);
        }

        @ParameterizedTest
        @ValueSource(strings = {"1","2", "3"})
        @Tag("unit")
        @DisplayName("Тест на выброс PositionOccupiedException")
        void saveSectionPositionOccupiedTest(String number) {

            section.setPersonelNumber(number);

            District district = new District(
                    UUID.fromString("1d30f1c3-e70d-42a0-a3d3-58a5c2d50d04"),
                    CENTRALNY,
                    "Находится со мной в здании",
                    2,
                    LocalDateTime.of(2025,07,30, 15,17,00,000),
                    LocalDateTime.of(2025,07,30, 15,17,00,000),
                    List.of(section)
            );

            createRequest.setPersonelNumber(number);

            when(createSectionMapper.toEntity(createRequest)).thenReturn(section);
            when(districtService.getDistrictByName(CENTRALNY)).thenReturn(district);

            PositionOccupiedException exception = assertThrows(
                    PositionOccupiedException.class,
                    () -> sectionService.saveSection(createRequest)
            );

            assertNotNull(exception);
            assertEquals("Позиция '" + number + "' уже занята", exception.getMessage());

            verify(createSectionMapper,times(1)).toEntity(createRequest);
            verify(districtService,times(1)).getDistrictByName(CENTRALNY);
            verify(sectionRepository, never()).save(section);
            verify(sectionRepository,never()).findById(id);
            verify(findSectionMapper,never()).entityToResponse(section);
        }
    }

    @Nested
    @DisplayName("Тесты на метод updateSection обновления данных района")
    class UpdateSectionTests {

        @ParameterizedTest
        @ValueSource(strings = {"1","2", "3"})
        @Tag("unit")
        @DisplayName("Тест с позитивным исходом")
        void updateSectionTest(String number) {

            updateRequest.setPersonelNumber(number);
            response.setPersonelNumber(number);

            when(sectionRepository.findById(id)).thenReturn(Optional.of(section));
            when(sectionRepository.existsSectionByPersonelNumber(updateRequest.getPersonelNumber())).thenReturn(false);
            doAnswer(invocationOnMock -> {
                section.setNumber(updateRequest.getNumber());
                return null;
            }).when(updateSectionMapper).update(updateRequest, section);
            when(sectionRepository.save(section)).thenReturn(section);
            when(sectionRepository.findById(id)).thenReturn(Optional.of(section));
            when(findSectionMapper.entityToResponse(section)).thenReturn(response);

            SectionResponse result = sectionService.updateSection(id, updateRequest);

            assertNotNull(result);
            assertEquals(response, result);

            verify(sectionRepository, times(1)).findById(id);
            verify(sectionRepository, times(1)).existsSectionByPersonelNumber(updateRequest.getPersonelNumber());
            verify(updateSectionMapper, times(1)).update(updateRequest, section);
            verify(sectionRepository, times(1)).save(section);
            verify(findSectionMapper, times(1)).entityToResponse(section);

        }

        @Test
        @Tag("unit")
        @DisplayName("Тест на выброс IdNotFoundException")
        void updateSectionIdNotFoundTest() {
            updateRequest.setNumber(1);

            when(sectionRepository.findById(badId)).thenReturn(Optional.empty());

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
        @ValueSource(strings = {"1","2", "3"})
        @Tag("unit")
        @DisplayName("Тест на выброс PositionOccupiedException")
        void updateSectionPositionOccupiedTest(String number) {

            updateRequest.setPersonelNumber(number);
            section.setPersonelNumber(number);

            when(sectionRepository.findById(id)).thenReturn(Optional.of(section));
            when(sectionRepository.existsSectionByPersonelNumber(updateRequest.getPersonelNumber())).thenReturn(true);

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
            assertEquals(section.getNumber() + " имеет сотрудников, удаление запрещено", exception.getMessage());

            verify(sectionRepository, times(1)).findById(id);
        }
    }
}
