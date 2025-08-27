package sadupstaff.sectionemployee;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
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
import sadupstaff.entity.district.District;
import sadupstaff.entity.district.Section;
import sadupstaff.entity.district.SectionEmployee;
import sadupstaff.enums.PositionSectionEmployeeEnum;
import sadupstaff.exception.IdNotFoundException;
import sadupstaff.exception.PositionOccupiedException;
import sadupstaff.exception.sectionemployee.MaxEmployeeInSectionException;
import sadupstaff.mapper.sectionemployee.CreateSectionEmployeeMapper;
import sadupstaff.mapper.sectionemployee.FindSectionEmployeeMapper;
import sadupstaff.mapper.sectionemployee.UpdateSectionEmployeeMapper;
import sadupstaff.repository.SectionEmployeeRepository;
import sadupstaff.service.section.SectionServiceImpl;
import sadupstaff.service.sectionemployee.SectionEmployeeServiceImpl;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Nested
    @DisplayName("Тесты на метод getAllSectionEmployees поиска всех сотрудников")
    class GetAllSectionEmployeeTests {

        @Test
        @Tag("unit")
        @DisplayName("Тест с позитивным исходом")
        void getAllSectionEmployeesTest() {

            when(sectionEmployeeRepository.findAll()).thenReturn(List.of(sectionEmployee));
            when(findSectionEmployeeMapper.entityToResponse(sectionEmployee)).thenReturn(response);

            List<SectionEmployeeResponse> result = sectionEmployeeService.getAllSectionEmployee();

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(response, result.get(0));

            verify(sectionEmployeeRepository, times(1)).findAll();
            verify(findSectionEmployeeMapper, times(1)).entityToResponse(sectionEmployee);
        }
    }

    @Nested
    @DisplayName("Тесты на метод getSectionEmployee поиска сотрудника по id")
    class GetSectionEmployeeByIdTests {

        @Test
        @Tag("unit")
        @DisplayName("Тест с позитивным исходом")
        void getSectionEmployeeByIdTest() {

            when(sectionEmployeeRepository.findById(id)).thenReturn(Optional.of(sectionEmployee));
            when(findSectionEmployeeMapper.entityToResponse(sectionEmployee)).thenReturn(response);

            SectionEmployeeResponse result = sectionEmployeeService.getSectionEmployee(id);

            assertNotNull(result);
            assertEquals(response, result);

            verify(sectionEmployeeRepository, times(1)).findById(id);
            verify(findSectionEmployeeMapper, times(1)).entityToResponse(sectionEmployee);
        }

        @Test
        @Tag("unit")
        @DisplayName("Тест с выбросом IdNotFoundException")
        void getSectionEmployeeByIdNotFoundIdTest() {

            when(sectionEmployeeRepository.findById(badId)).thenReturn(Optional.empty());

            IdNotFoundException exception = assertThrows(IdNotFoundException.class,
                    () -> sectionEmployeeService.getSectionEmployee(badId));

            assertNotNull(exception);
            assertEquals("Id '" + badId + "' не найден", exception.getMessage());

            verify(sectionEmployeeRepository, times(1)).findById(badId);
            verify(findSectionEmployeeMapper, never()).entityToResponse(sectionEmployee);
        }
    }

    @Nested
    @DisplayName("Тесты на метод saveSectionEmployee сохранения сотрудника")
    class SaveSectionEmployeeTests {

        @ParameterizedTest
        @EnumSource(PositionSectionEmployeeEnum.class)
        @Tag("unit")
        @DisplayName("Тест с позитивным исходом")
        void saveSectionEmployeeTest(PositionSectionEmployeeEnum position) {

            Section section = new Section(
                    UUID.fromString("3d30f1c3-e70d-42a0-a3d3-58a5c2d50d04"),
                    "M540000",
                    "1",
                    3,
                    LocalDateTime.of(2025,07,30, 15,17,00,000),
                    LocalDateTime.of(2025,07,30, 15,17,00,000),
                    new District(),
                    List.of()
            );

            sectionEmployee.setPosition(position);
            createRequest.setPosition(position);
            response.setPosition(position.getStringConvert());

            when(createSectionEmployeeMapper.toEntity(createRequest)).thenReturn(sectionEmployee);
            when(sectionService.getSectionByName(createRequest.getSectionName())).thenReturn(section);
            when(sectionEmployeeRepository.save(sectionEmployee)).thenReturn(sectionEmployee);
            when(sectionEmployeeRepository.findById(id)).thenReturn(Optional.of(sectionEmployee));
            when(findSectionEmployeeMapper.entityToResponse(sectionEmployee)).thenReturn(response);

            SectionEmployeeResponse result = sectionEmployeeService.saveNewSectionEmployee(createRequest);

            assertNotNull(result);
            assertEquals(response, result);

            verify(createSectionEmployeeMapper, times(1)).toEntity(createRequest);
            verify(sectionService, times(1)).getSectionByName(createRequest.getSectionName());
            verify(sectionEmployeeRepository, times(1)).findById(id);
            verify(sectionEmployeeRepository, times(1)).save(sectionEmployee);
            verify(findSectionEmployeeMapper, times(1)).entityToResponse(sectionEmployee);
        }

        @ParameterizedTest
        @EnumSource(PositionSectionEmployeeEnum.class)
        @Tag("unit")
        @DisplayName("Тест на выброс PositionOccupiedException")
        void saveSectionEmployeePositionOccupiedTest(PositionSectionEmployeeEnum position) {

            createRequest.setPosition(position);
            sectionEmployee.setPosition(position);

            Section section = new Section(
                    UUID.fromString("3d30f1c3-e70d-42a0-a3d3-58a5c2d50d04"),
                    "M540000",
                    "1",
                    3,
                    LocalDateTime.of(2025,07,30, 15,17,00,000),
                    LocalDateTime.of(2025,07,30, 15,17,00,000),
                    new District(),
                    List.of(sectionEmployee)
            );

            when(createSectionEmployeeMapper.toEntity(createRequest)).thenReturn(sectionEmployee);
            when(sectionService.getSectionByName(createRequest.getSectionName())).thenReturn(section);

            PositionOccupiedException exception = assertThrows(
                    PositionOccupiedException.class,
                    () -> sectionEmployeeService.saveNewSectionEmployee(createRequest)
            );

            assertNotNull(exception);
            assertEquals("Позиция '" + position.getStringConvert() + "' уже занята", exception.getMessage());

            verify(createSectionEmployeeMapper, times(1)).toEntity(createRequest);
            verify(sectionService, times(1)).getSectionByName(createRequest.getSectionName());
            verify(sectionEmployeeRepository, never()).findById(id);
            verify(sectionEmployeeRepository, never()).save(sectionEmployee);
            verify(findSectionEmployeeMapper, never()).entityToResponse(sectionEmployee);
        }

        @ParameterizedTest
        @EnumSource(PositionSectionEmployeeEnum.class)
        @Tag("unit")
        @DisplayName("Тест на выброс MaxEmployeeInSectionException")
        void saveEmployeeMaxEmployeeInSectionTest(PositionSectionEmployeeEnum position) {

            sectionEmployee.setPosition(position);
            createRequest.setPosition(position);

            Section section = new Section(
                    UUID.fromString("3d30f1c3-e70d-42a0-a3d3-58a5c2d50d04"),
                    "M540000",
                    "1",
                    3,
                    LocalDateTime.of(2025,07,30, 15,17,00,000),
                    LocalDateTime.of(2025,07,30, 15,17,00,000),
                    new District(),
                    List.of(sectionEmployee, sectionEmployee, sectionEmployee)
            );

            when(createSectionEmployeeMapper.toEntity(createRequest)).thenReturn(sectionEmployee);
            when(sectionService.getSectionByName(createRequest.getSectionName())).thenReturn(section);

            MaxEmployeeInSectionException exception = assertThrows(
                    MaxEmployeeInSectionException .class,
                    () -> sectionEmployeeService.saveNewSectionEmployee(createRequest)
            );

            assertNotNull(exception);
            assertEquals("В '" + section.getName() + "' максимальное количество сотрудников", exception.getMessage());

            verify(createSectionEmployeeMapper, times(1)).toEntity(createRequest);
            verify(sectionService, times(1)).getSectionByName(createRequest.getSectionName());
            verify(sectionEmployeeRepository, never()).findById(id);
            verify(sectionEmployeeRepository, never()).save(sectionEmployee);
            verify(findSectionEmployeeMapper, never()).entityToResponse(sectionEmployee);
        }
    }

    @Nested
    @DisplayName("Тесты на метод updateSectionEmployee обновления данных сотрудника")
    class UpdateSectionEmployeeTests {

        @ParameterizedTest
        @EnumSource(PositionSectionEmployeeEnum.class)
        @Tag("unit")
        @DisplayName("Тест с позитивным исходом")
        void updateSectionEmployeeTest(PositionSectionEmployeeEnum position) {

            updateRequest.setPosition(position);
            response.setPosition(position.getStringConvert());

            when(sectionEmployeeRepository.findById(id)).thenReturn(Optional.of(sectionEmployee));
            when(sectionEmployeeRepository.existsSectionEmployeeByPosition(position)).thenReturn(false);
            doAnswer(invocation -> {
                sectionEmployee.setPosition(position);
                return null;
            }).when(updateSectionEmployeeMapper).updateSectionEmployeeData(updateRequest, sectionEmployee);
            when(sectionEmployeeRepository.save(sectionEmployee)).thenReturn(sectionEmployee);
            when(findSectionEmployeeMapper.entityToResponse(sectionEmployee)).thenReturn(response);

            SectionEmployeeResponse result = sectionEmployeeService.updateSectionEmployee(id, updateRequest);

            assertNotNull(result);
            assertEquals(response, result);

            verify(sectionEmployeeRepository, times(1)).findById(id);
            verify(sectionEmployeeRepository, times(1)).existsSectionEmployeeByPosition(position);
            verify(updateSectionEmployeeMapper, times(1)).updateSectionEmployeeData(updateRequest, sectionEmployee);
            verify(sectionEmployeeRepository, times(1)).save(sectionEmployee);
            verify(findSectionEmployeeMapper, times(1)).entityToResponse(sectionEmployee);
        }

        @Test
        @Tag("unit")
        @DisplayName("Тест на выброс IdNotFoundException")
        void updateSectionEmployeeIdNotFoundTest() {

            when(sectionEmployeeRepository.findById(badId)).thenReturn(Optional.empty());

            IdNotFoundException exception = assertThrows(
                    IdNotFoundException.class,
                    () -> sectionEmployeeService.updateSectionEmployee(badId, updateRequest)
            );

            assertNotNull(exception);
            assertEquals("Id '" + badId + "' не найден", exception.getMessage());

            verify(sectionEmployeeRepository, times(1)).findById(badId);
            verify(sectionEmployeeRepository, never()).existsSectionEmployeeByPosition(any(PositionSectionEmployeeEnum.class));
            verify(updateSectionEmployeeMapper, never()).updateSectionEmployeeData(updateRequest, sectionEmployee);
            verify(sectionEmployeeRepository, never()).save(sectionEmployee);
            verify(findSectionEmployeeMapper, never()).entityToResponse(sectionEmployee);
        }

        @ParameterizedTest
        @EnumSource(PositionSectionEmployeeEnum.class)
        @Tag("unit")
        @DisplayName("Тест на выброс PositionOccupiedException")
        void updateSectionEmployeePositionOccupiedTest(PositionSectionEmployeeEnum position) {

            updateRequest.setPosition(position);

            when(sectionEmployeeRepository.findById(id)).thenReturn(Optional.of(sectionEmployee));
            when(sectionEmployeeRepository.existsSectionEmployeeByPosition(position)).thenReturn(true);

            PositionOccupiedException exception = assertThrows(
                    PositionOccupiedException.class,
                    () -> sectionEmployeeService.updateSectionEmployee(id, updateRequest)
            );

            assertNotNull(exception);
            assertEquals("Позиция '" + position.getStringConvert() + "' уже занята", exception.getMessage());

            verify(sectionEmployeeRepository, times(1)).findById(id);
            verify(sectionEmployeeRepository, times(1)).existsSectionEmployeeByPosition(any(PositionSectionEmployeeEnum.class));
            verify(updateSectionEmployeeMapper, never()).updateSectionEmployeeData(updateRequest, sectionEmployee);
            verify(sectionEmployeeRepository, never()).save(sectionEmployee);
            verify(findSectionEmployeeMapper, never()).entityToResponse(sectionEmployee);
        }
    }

    @Nested
    @DisplayName("Тесты на метод deleteSectionEmployee удаления сотрудника по id")
    class DeleteEmployeeTests {

        @Test
        @Tag("unit")
        @DisplayName("Тест с позитивным исходом")
        void deleteSectionEmployeeByIdTest() {

            when(sectionEmployeeRepository.findById(id)).thenReturn(Optional.of(sectionEmployee));
            doAnswer(invocation -> {
                return null;
            }).when(sectionEmployeeRepository).deleteById(id);

            sectionEmployeeService.deleteSectionEmployee(id);

            verify(sectionEmployeeRepository, times(1)).findById(id);
            verify(sectionEmployeeRepository, times(1)).deleteById(id);
        }

        @Test
        @Tag("unit")
        @DisplayName("Тест на выброс IdNotFoundException")
        void deleteSectionEmployeeIdNotFoundTest() {

            when(sectionEmployeeRepository.findById(badId)).thenReturn(Optional.empty());

            IdNotFoundException exception = assertThrows(
                    IdNotFoundException.class,
                    () -> sectionEmployeeService.deleteSectionEmployee(badId)
            );

            assertNotNull(exception);
            assertEquals("Id '" + badId + "' не найден", exception.getMessage());

            verify(sectionEmployeeRepository, times(1)).findById(badId);
            verify(sectionEmployeeRepository, never()).deleteById(badId);
        }
    }
}
