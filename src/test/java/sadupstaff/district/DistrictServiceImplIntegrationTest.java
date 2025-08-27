package sadupstaff.district;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
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
import sadupstaff.dto.request.create.CreateDistrictRequest;
import sadupstaff.dto.request.update.UpdateDistrictRequest;
import sadupstaff.dto.response.DistrictResponse;
import sadupstaff.entity.district.District;
import sadupstaff.entity.district.Section;
import sadupstaff.enums.DistrictNameEnum;
import sadupstaff.exception.IdNotFoundException;
import sadupstaff.exception.PositionOccupiedException;
import sadupstaff.exception.district.DeleteDistrictException;
import sadupstaff.mapper.district.CreateDistrictMapper;
import sadupstaff.mapper.district.FindDistrictMapper;
import sadupstaff.mapper.district.UpdateDistrictMapper;
import sadupstaff.repository.DistrictRepository;
import sadupstaff.service.district.DistrictServiceImpl;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;
import static sadupstaff.enums.DistrictNameEnum.CENTRALNY;

@Log4j2
@SpringBootTest()
@Testcontainers
@Transactional
@DisplayName("Integration тесты методов DistrictServiceImpl")
public class DistrictServiceImplIntegrationTest {

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
    private DistrictRepository districtRepository;

    @MockitoSpyBean
    private UpdateDistrictMapper updateDistrictMapper;

    @MockitoSpyBean
    private FindDistrictMapper findDistrictMapper;

    @MockitoSpyBean
    private CreateDistrictMapper createDistrictMapper;

    @Autowired
    private DistrictServiceImpl districtService;

    private District district;
    private CreateDistrictRequest createRequest;
    private UpdateDistrictRequest updateRequest;
    private UUID id1;
    private UUID id2;
    private UUID badId;

    @BeforeEach
    void setUp() {

        district = new District(
                UUID.fromString("1d30f1c3-e70d-42a0-a3d3-58a5c2d50d04"),
                CENTRALNY,
                "Находится со мной в здании",
                2,
                LocalDateTime.of(2025,07,30, 15,17,00,000),
                LocalDateTime.of(2025,07,30, 15,17,00,000),
                List.of()
        );

        id1 = district.getId();
        id2 = UUID.fromString("2d30f1c3-e70d-42a0-a3d3-58a5c2d50d04");
        badId = UUID.fromString("2d30f1c3-e70d-42a0-a3d3-58a5c2d50d05");

        createRequest = new CreateDistrictRequest(
                CENTRALNY,
                2,
                "Находитсясо мной в здании"

        );

        updateRequest = new UpdateDistrictRequest();
    }

    @Nested
    @Transactional
    @DisplayName("Тесты на метод getAllDistrict поиска всех районов")
    class GetAllDistrictsTests {

        @Test
        @Tag("integration")
        @DisplayName("Тест с позитивным исходом")
        void getAllDistrictsTest() {

            List<DistrictResponse> result = districtService.getAllDistrict();

            log.info(result.get(0));

            assertEquals(2, result.size());
            assertFalse(result.get(0).getSections().isEmpty());
            assertTrue(result.get(1).getSections().isEmpty());

            verify(districtRepository).findAll();
            verify(findDistrictMapper, times(2)).entityToResponse(any(District.class));
        }
    }

    @Nested
    @Transactional
    @DisplayName("Тесты на метод getDistrictById поиска района по id")
    class GetDistrictByIdTests {

        @ParameterizedTest
        @ValueSource(strings = {
                "1d30f1c3-e70d-42a0-a3d3-58a5c2d50d04",
                "2d30f1c3-e70d-42a0-a3d3-58a5c2d50d04"
        })
        @Tag("integration")
        @DisplayName("Тест с позитивным исходом")
        void getDistrictByIdTest(String id) {

            DistrictResponse result = districtService.getDistrictById(UUID.fromString(id));

            assertNotNull(result);

            verify(districtRepository, times(1)).findById(any(UUID.class));
            verify(findDistrictMapper, times(1)).entityToResponse(any(District.class));
        }

        @Test
        @Tag("integration")
        @DisplayName("Тест с выбросом IdNotFoundException")
        void getDistrictByIdNotFoundIdTest() {

            IdNotFoundException exception = assertThrows(
                    IdNotFoundException.class,
                    () -> districtService.getDistrictById(badId)
            );

            assertEquals("Id '" + badId + "' не найден", exception.getMessage());

            verify(districtRepository, times(1)).findById(any(UUID.class));
            verify(findDistrictMapper, never()).entityToResponse(any(District.class));
        }
    }

    @Nested
    @Transactional
    @DisplayName("Тесты на метод getDistrictByName поиска района по имени")
    class GetDistrictByNameTests {

        @ParameterizedTest
        @EnumSource(
                value = DistrictNameEnum.class,
                mode = EnumSource.Mode.INCLUDE,
                names = {"CENTRALNY", "ZHELEZNODOROZHHNY"}
        )
        @Tag("integration")
        @DisplayName("Тест с позитивным исходом")
        void getDistrictByNameTest(DistrictNameEnum name) {

            District result = districtService.getDistrictByName(name);

            assertNotNull(result);
            assertNotNull(result.getId());
            assertEquals(name, result.getName());

            verify(districtRepository, times(1)).findDistrictByName(name);
        }
    }

    @Nested
    @Transactional
    @DisplayName("Тесты на метод saveDistrict сохранения района")
    class SaveDistrictTests {

        @ParameterizedTest
        @EnumSource(
                value = DistrictNameEnum.class,
                mode = EnumSource.Mode.EXCLUDE,
                names = {"CENTRALNY", "ZHELEZNODOROZHHNY"}
        )
        @Tag("integration")
        @DisplayName("Тест с позитивным исходом")
        void saveDistrictTest(DistrictNameEnum name) {

            createRequest.setName(name);

            DistrictResponse result = districtService.saveDistrict(createRequest);

            assertNotNull(result);
            assertEquals(name.getStringConvert(), result.getName());

            verify(districtRepository, times(1)).existsDistinctByName(name);
            verify(createDistrictMapper, times(1)).toEntity(createRequest);
            verify(districtRepository, times(1)).existsDistinctByName(any(DistrictNameEnum.class));
            verify(districtRepository, times(1)).save(any(District.class));
            verify(districtRepository, times(1)).findById(any(UUID.class));
            verify(findDistrictMapper, times(1)).entityToResponse(any(District.class));
        }

        @ParameterizedTest
        @EnumSource(
                value = DistrictNameEnum.class,
                mode = EnumSource.Mode.INCLUDE,
                names = {"CENTRALNY", "ZHELEZNODOROZHHNY"}
        )
        @Tag("integration")
        @DisplayName("Тест на выброс PositionOccupiedException")
        void saveDistrictPositionOccupiedTest(DistrictNameEnum name) {

            createRequest.setName(name);

            PositionOccupiedException exception = assertThrows(
                    PositionOccupiedException.class,
                    () -> districtService.saveDistrict(createRequest)
            );

            assertEquals("Позиция '" + name.getStringConvert() + "' уже занята", exception.getMessage());

            verify(districtRepository, times(1)).existsDistinctByName(name);
            verify(createDistrictMapper, never()).toEntity(any(CreateDistrictRequest.class));
            verify(districtRepository, never()).save(any(District.class));
            verify(districtRepository, never()).findById(any(UUID.class));
            verify(findDistrictMapper, never()).entityToResponse(any(District.class));
        }
    }

    @Nested
    @Transactional
    @DisplayName("Тесты на метод updateDistrict обновления данных района")
    class UpdateDistrictTests {

        @ParameterizedTest
        @EnumSource(
                value = DistrictNameEnum.class,
                mode = EnumSource.Mode.EXCLUDE,
                names = {"CENTRALNY", "ZHELEZNODOROZHHNY"}
        )
        @Tag("integration")
        @DisplayName("Тест с позитивным исходом")
        void updateDistrictTest(DistrictNameEnum name) {

            updateRequest.setName(name);
            updateRequest.setDescription(name.getStringConvert());

            DistrictResponse result = districtService.updateDistrict(id1, updateRequest);

            assertNotNull(result);
            assertEquals(result.getName(), name.getStringConvert());

            verify(districtRepository, times(1)).findById(any(UUID.class));
            verify(districtRepository, times(1)).existsDistinctByName(name);
            verify(updateDistrictMapper, times(1)).updateDistrictData(any(UpdateDistrictRequest.class), any(District.class));
            verify(districtRepository, times(1)).save(any(District.class));
            verify(findDistrictMapper, times(1)).entityToResponse(any(District.class));
        }

        @Test
        @Tag("integration")
        @DisplayName("Тест на выброс IdNotFoundException")
        void updateDistrictIdNotFoundTest() {

            IdNotFoundException exception = assertThrows(
                    IdNotFoundException.class,
                    () -> districtService.updateDistrict(badId, updateRequest)
            );

            assertEquals("Id '" + badId + "' не найден", exception.getMessage());

            verify(districtRepository, times(1)).findById(any(UUID.class));
            verify(districtRepository, never()).existsDistinctByName(any(DistrictNameEnum.class));
            verify(updateDistrictMapper, never()).updateDistrictData(any(UpdateDistrictRequest.class), any(District.class));
            verify(districtRepository, never()).save(any(District.class));
            verify(findDistrictMapper, never()).entityToResponse(any(District.class));

        }

        @ParameterizedTest
        @EnumSource(
                value = DistrictNameEnum.class,
                mode = EnumSource.Mode.INCLUDE,
                names = {"CENTRALNY", "ZHELEZNODOROZHHNY"}
        )
        @Tag("integration")
        @DisplayName("Тест на выброс PositionOccupiedException")
        void updateDistrictPositionOccupiedTest(DistrictNameEnum name) {

            updateRequest.setName(name);

            PositionOccupiedException exception = assertThrows(
                    PositionOccupiedException.class,
                    () -> districtService.updateDistrict(id1, updateRequest)
            );

            assertEquals("Позиция '" + name.getStringConvert() + "' уже занята", exception.getMessage());

            verify(districtRepository, times(1)).findById(any(UUID.class));
            verify(districtRepository, times(1)).existsDistinctByName(name);
            verify(updateDistrictMapper, never()).updateDistrictData(any(UpdateDistrictRequest.class), any(District.class));
            verify(districtRepository, never()).save(any(District.class));
            verify(findDistrictMapper, never()).entityToResponse(any(District.class));
        }
    }

    @Nested
    @Transactional
    @DisplayName("Тесты на метод deleteDistrict удаления района по id")
    class DeleteDistrictByIdTests {

        @Test
        @Transactional
        @Tag("integration")
        @DisplayName("Тест с позитивным исходом")
        void deleteDistrictByIdTest() {

            districtService.deleteDistrict(id2);

            verify(districtRepository, times(1)).findById(any(UUID.class));
            verify(districtRepository, times(1)).deleteById(any(UUID.class));
        }

        @Test
        @Transactional
        @Tag("integration")
        @DisplayName("Тест на выброс IdNotFoundException")
        void deleteDistrictIdNotFoundTest() {

            IdNotFoundException exception = assertThrows(
                    IdNotFoundException.class,
                    () -> districtService.deleteDistrict(badId)
            );

            assertEquals("Id '" + badId + "' не найден", exception.getMessage());

            verify(districtRepository, times(1)).findById(any(UUID.class));
            verify(districtRepository, never()).deleteById(any(UUID.class));
        }

        @Test
        @Transactional
        @Tag("integration")
        @DisplayName("Тест на выброс DeleteDistrictException")
        void deleteDistrictByIdDeleteDistrictExceptionTest() {

            district.setSections(List.of(new Section()));

            DeleteDistrictException exception = assertThrows(
                    DeleteDistrictException.class,
                    () -> districtService.deleteDistrict(id1)
            );

            assertFalse(district.getSections().isEmpty());
            assertEquals(district.getName().getStringConvert() + " содержит участки, удаление запрещено",exception.getMessage());

            verify(districtRepository, times(1)).findById(any(UUID.class));
            verify(districtRepository, never()).deleteById(any(UUID.class));
        }
    }
}
