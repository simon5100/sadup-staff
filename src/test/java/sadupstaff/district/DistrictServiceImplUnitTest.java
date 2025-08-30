package sadupstaff.district;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static sadupstaff.enums.DistrictNameEnum.CENTRALNY;

@Slf4j
@ExtendWith(MockitoExtension.class)
@DisplayName("Unit тесты методов DistrictServiceImpl")
public class DistrictServiceImplUnitTest {

    @Mock
    private DistrictRepository districtRepository;

    @Mock
    private UpdateDistrictMapper updateDistrictMapper;

    @Mock
    private FindDistrictMapper findDistrictMapper;

    @Mock
    private CreateDistrictMapper createDistrictMapper;

    @InjectMocks
    private DistrictServiceImpl districtService;

    private District district;
    private DistrictResponse response;
    private CreateDistrictRequest createRequest;
    private UpdateDistrictRequest updateRequest;
    private UUID id;
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

        id = district.getId();
        badId = UUID.fromString("2d30f1c3-e70d-42a0-a3d3-58a5c2d50d05");

        createRequest = new CreateDistrictRequest(
                CENTRALNY,
                2,
                "Находитсясо мной в здании"

        );

        response = new DistrictResponse(
                CENTRALNY.getStringConvert(),
                "Находится со мной в здании",
                LocalDateTime.of(2025,07,30, 15,17,00,000),
                LocalDateTime.of(2025,07,30, 15,17,00,000),
                List.of()
        );

        updateRequest = new UpdateDistrictRequest();
    }

    @Nested
    @DisplayName("Тесты на метод getAllDistrict поиска всех районов")
    class GetAllDistrictsTests {

        @Test
        @Tag("unit")
        @DisplayName("Тест с позитивным исходом")
        void getAllDistrictsTest() {

            when(districtRepository.findAll()).thenReturn(List.of(district));
            when(findDistrictMapper.entityToResponse(district)).thenReturn(response);

            List<DistrictResponse> result = districtService.getAllDistrict();

            assertEquals(1, result.size());
            assertEquals(response, result.get(0));

            verify(districtRepository).findAll();
            verify(findDistrictMapper, times(1)).entityToResponse(district);

        }
    }

    @Nested
    @DisplayName("Тесты на метод getDistrictById поиска района по id")
    class GetDistrictByIdTests {

        @Test
        @Tag("unit")
        @DisplayName("Тест с позитивным исходом")
        void getDistrictByIdTest() {

            when(districtRepository.findById(id)).thenReturn(Optional.of(district));
            when(findDistrictMapper.entityToResponse(district)).thenReturn(response);

            DistrictResponse result = districtService.getDistrictById(id);

            assertEquals(result, response);

            verify(districtRepository, times(1)).findById(id);
            verify(findDistrictMapper, times(1)).entityToResponse(district);
        }

        @Test
        @Tag("unit")
        @DisplayName("Тест с выбросом IdNotFoundException")
        void getDistrictByIdNotFoundIdTest() {

            when(districtRepository.findById(badId)).thenReturn(Optional.empty());

            IdNotFoundException exception = assertThrows(
                    IdNotFoundException.class,
                    () -> districtService.getDistrictById(badId)
            );

            assertEquals("Id '" + badId + "' не найден", exception.getMessage());

            verify(districtRepository, times(1)).findById(badId);
            verify(findDistrictMapper, never()).entityToResponse(district);
        }
    }

    @Nested
    @DisplayName("Тесты на метод getDistrictByName поиска района по имени")
    class GetDistrictByNameTests {

        @ParameterizedTest
        @EnumSource(DistrictNameEnum.class)
        @Tag("unit")
        @DisplayName("Тест с позитивным исходом")
        void getDistrictByNameTest(DistrictNameEnum name) {

            district.setName(name);

            when(districtRepository.findDistrictByName(name)).thenReturn(district);

            District result = districtService.getDistrictByName(name);

            assertEquals(result, district);
        }
    }

    @Nested
    @DisplayName("Тесты на метод saveDistrict сохранения района")
    class SaveDistrictTests {

        @ParameterizedTest
        @EnumSource(DistrictNameEnum.class)
        @Tag("unit")
        @DisplayName("Тест с позитивным исходом")
        void saveDistrictTest(DistrictNameEnum name) {

            createRequest.setName(name);
            district.setName(name);
            response.setName(name.getStringConvert());

            when(districtRepository.existsDistinctByName(district.getName())).thenReturn(false);
            when(createDistrictMapper.toEntity(createRequest)).thenReturn(district);
            when(districtRepository.save(district)).thenReturn(district);
            when(districtRepository.findById(district.getId())).thenReturn(Optional.of(district));
            when(findDistrictMapper.entityToResponse(district)).thenReturn(response);

            DistrictResponse result = districtService.saveDistrict(createRequest);

            assertEquals(result, response);

            verify(createDistrictMapper, times(1)).toEntity(createRequest);
            verify(districtRepository, times(1)).existsDistinctByName(district.getName());
            verify(districtRepository, times(1)).save(district);
            verify(districtRepository, times(1)).findById(district.getId());
            verify(findDistrictMapper, times(1)).entityToResponse(district);
        }

        @ParameterizedTest
        @EnumSource(DistrictNameEnum.class)
        @Tag("unit")
        @DisplayName("Тест на выброс PositionOccupiedException")
        void saveDistrictPositionOccupiedTest(DistrictNameEnum name) {

            createRequest.setName(name);
            district.setName(name);

            when(districtRepository.existsDistinctByName(createRequest.getName())).thenReturn(true);

            PositionOccupiedException exception = assertThrows(
                    PositionOccupiedException.class,
                    () -> districtService.saveDistrict(createRequest)
            );

            assertEquals("Позиция '" + name.getStringConvert() + "' уже занята", exception.getMessage());

            verify(districtRepository, times(1)).existsDistinctByName(district.getName());
            verify(createDistrictMapper, never()).toEntity(createRequest);
            verify(districtRepository, never()).save(district);
            verify(districtRepository, never()).findById(district.getId());
            verify(findDistrictMapper, never()).entityToResponse(district);
        }
    }

    @Nested
    @DisplayName("Тесты на метод updateDistrict обновления данных района")
    class UpdateDistrictTests {

        @ParameterizedTest
        @EnumSource(DistrictNameEnum.class)
        @Tag("unit")
        @DisplayName("Тест с позитивным исходом")
        void updateDistrictTest(DistrictNameEnum name) {

            updateRequest.setName(name);
            updateRequest.setDescription(name.getStringConvert());
            response.setName(name.getStringConvert());
            response.setDescription(name.getStringConvert());

            when(districtRepository.findById(id)).thenReturn(Optional.of(district));
            when(districtRepository.existsDistinctByName(name)).thenReturn(false);
            doAnswer(invocation -> {
                district.setName(name);
                district.setDescription(name.getStringConvert());
                return null;
            }).when(updateDistrictMapper).updateDistrictData(updateRequest, district);
            when(districtRepository.save(district)).thenReturn(district);
            when(findDistrictMapper.entityToResponse(district)).thenReturn(response);

            DistrictResponse result = districtService.updateDistrict(id, updateRequest);

            log.info(result.getName());

            assertEquals(result.getName(), name.getStringConvert());
            assertEquals(result, response);

            verify(districtRepository, times(1)).findById(id);
            verify(districtRepository, times(1)).existsDistinctByName(name);
            verify(updateDistrictMapper, times(1)).updateDistrictData(updateRequest, district);
            verify(districtRepository, times(1)).save(district);
            verify(findDistrictMapper, times(1)).entityToResponse(district);
        }

        @Test
        @Tag("unit")
        @DisplayName("Тест на выброс IdNotFoundException")
        void updateDistrictIdNotFoundTest() {

            when(districtRepository.findById(badId)).thenReturn(Optional.empty());

            IdNotFoundException exception = assertThrows(
                    IdNotFoundException.class,
                    () -> districtService.updateDistrict(badId, updateRequest)
                    );

            assertEquals("Id '" + badId + "' не найден", exception.getMessage());

            verify(districtRepository, times(1)).findById(badId);
            verify(districtRepository, never()).existsDistinctByName(any(DistrictNameEnum.class));
            verify(updateDistrictMapper, never()).updateDistrictData(updateRequest, district);
            verify(districtRepository, never()).save(district);
            verify(findDistrictMapper, never()).entityToResponse(district);

        }

        @ParameterizedTest
        @EnumSource(DistrictNameEnum.class)
        @Tag("unit")
        @DisplayName("Тест на выброс PositionOccupiedException")
        void updateDistrictPositionOccupiedTest(DistrictNameEnum name) {

            updateRequest.setName(name);

            when(districtRepository.findById(id)).thenReturn(Optional.of(district));
            when(districtRepository.existsDistinctByName(name)).thenReturn(true);

            PositionOccupiedException exception = assertThrows(
                    PositionOccupiedException.class,
                    () -> districtService.updateDistrict(id, updateRequest)
            );

            assertEquals("Позиция '" + name.getStringConvert() + "' уже занята", exception.getMessage());

            verify(districtRepository, times(1)).findById(id);
            verify(districtRepository, times(1)).existsDistinctByName(name);
            verify(updateDistrictMapper, never()).updateDistrictData(updateRequest, district);
            verify(districtRepository, never()).save(district);
            verify(findDistrictMapper, never()).entityToResponse(district);
        }
    }

    @Nested
    @DisplayName("Тесты на метод deleteDistrict удаления района по id")
    class DeleteDistrictByIdTests {

        @Test
        @Tag("unit")
        @DisplayName("Тест с позитивным исходом")
        void deleteDistrictByIdTest() {

            when(districtRepository.findById(id)).thenReturn(Optional.of(district));
            doAnswer(invocation -> {
                return null;}).when(districtRepository).deleteById(id);

            districtService.deleteDistrict(id);

            assertTrue(district.getSections().isEmpty());

            verify(districtRepository, times(1)).findById(id);
            verify(districtRepository, times(1)).deleteById(id);
        }

        @Test
        @Tag("unit")
        @DisplayName("Тест на выброс IdNotFoundException")
        void deleteDistrictIdNotFoundTest() {

            when(districtRepository.findById(badId)).thenReturn(Optional.empty());

            IdNotFoundException exception = assertThrows(
                    IdNotFoundException.class,
                    () -> districtService.deleteDistrict(badId)
                    );

            assertEquals("Id '" + badId + "' не найден", exception.getMessage());

            verify(districtRepository, times(1)).findById(badId);
            verify(districtRepository, never()).deleteById(badId);
        }

        @Test
        @Tag("unit")
        @DisplayName("Тест на выброс DeleteDistrictException")
        void deleteDistrictByIdDeleteDistrictExceptionTest() {

            district.setSections(List.of(new Section()));

            when(districtRepository.findById(id)).thenReturn(Optional.of(district));
            DeleteDistrictException exception = assertThrows(
                    DeleteDistrictException.class,
                    () -> districtService.deleteDistrict(id)
            );

            assertFalse(district.getSections().isEmpty());
            assertEquals(district.getName().getStringConvert() + " содержит участки, удаление запрещено",exception.getMessage());

            verify(districtRepository, times(1)).findById(id);
            verify(districtRepository, never()).deleteById(id);
        }
    }
}