package sadupstaff.department;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sadupstaff.dto.request.create.CreateDepartmentRequest;
import sadupstaff.dto.request.update.UpdateDepartmentRequest;
import sadupstaff.dto.response.DepartmentResponse;
import sadupstaff.entity.management.Department;
import sadupstaff.entity.management.Employee;
import sadupstaff.enums.DepartmentNameEnum;
import sadupstaff.exception.DepartmentNotFoundException;
import sadupstaff.exception.IdNotFoundException;
import sadupstaff.exception.PositionOccupiedException;
import sadupstaff.exception.department.DeleteDepartmentException;
import sadupstaff.mapper.department.CreateDepartmentMapper;
import sadupstaff.mapper.department.FindDepartmentMapper;
import sadupstaff.mapper.department.UpdateDepartmentMapper;
import sadupstaff.repository.DepartmentRepository;
import sadupstaff.service.department.DepartmentServiceImpl;
import java.time.LocalDateTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static sadupstaff.enums.DepartmentNameEnum.LEGAL_SUPPORT;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тесты методов сервиса отделов")
public class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private UpdateDepartmentMapper updateDepartmentMapper;

    @Mock
    private FindDepartmentMapper findDepartmentMapper;

    @Mock
    private CreateDepartmentMapper createDepartmentMapper;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private Department department;
    private DepartmentResponse response;
    private CreateDepartmentRequest createDepartmentRequest;
    private UpdateDepartmentRequest updateDepartmentRequest;
    private UUID id;
    private UUID badId;

    @BeforeEach
    void setUp() {
        department = new Department(
                UUID.fromString("2d30f1c3-e70d-42a0-a3d3-58a5c2d50d04"),
                LEGAL_SUPPORT,
                5,
                "обеспечивает правами",
                LocalDateTime.now(),
                LocalDateTime.now(),
                List.of()
        );

        id = department.getId();
        badId = UUID.fromString("2d30f1c3-e70d-42a0-a3d3-58a5c2d50d05");

        createDepartmentRequest = new CreateDepartmentRequest(
                "LEGAL_SUPPORT",
                5,
                "обеспечивает правами"
        );

        response = new DepartmentResponse();
        response.setName(department.getName().getStringConvert());
        response.setDescription(department.getDescription());
        response.setUpdatedAt(department.getUpdatedAt());
        response.setCreatedAt(department.getCreatedAt());
        response.setEmps(List.of());

        updateDepartmentRequest = new UpdateDepartmentRequest();
    }

    @Nested
    @DisplayName("Тесты на метод getAllDepartments поиска всех отделов")
    class GetAllDepartmentsTests {

        @Test
        @Tag("unit")
        @DisplayName("Тест с позитивным исходом")
        void getAllDepartmentsTest() {

            when(departmentRepository.findAll()).thenReturn(List.of(department));
            when(findDepartmentMapper.entityToResponse(department)).thenReturn(response);

            List<DepartmentResponse> result = departmentService.getAllDepartments();

            assertEquals(1, result.size());
            assertEquals(response, result.get(0));

            verify(departmentRepository).findAll();
            verify(findDepartmentMapper, times(1)).entityToResponse(department);
        }
    }

    @Nested
    @DisplayName("Тесты на метод getDepartmentById поиска отдела по id")
    class GetDepartmentByIdTests {

        @Test
        @Tag("unit")
        @DisplayName("Тест с позитивным исходом")
        void getDepartmentByIdTest() {

            when(departmentRepository.findById(id)).thenReturn(Optional.of(department));
            when(findDepartmentMapper.entityToResponse(department)).thenReturn(response);

            DepartmentResponse result = departmentService.getDepartmentById(id);

            assertEquals(result, response);
            verify(findDepartmentMapper, times(1)).entityToResponse(department);
        }

        @Test
        @Tag("unit")
        @DisplayName("Тест с выбросом IdNotFoundException")
        void getDepartmentByIdNotFoundIdTest() {

            when(departmentRepository.findById(badId)).thenReturn(Optional.empty());

            IdNotFoundException exception = assertThrows(
                    IdNotFoundException.class,
                    () -> departmentService.getDepartmentById(badId));

            assertEquals("Id '" + badId + "' не найден", exception.getMessage());

            verify(departmentRepository, times(1)).findById(badId);
        }
    }

    @Nested
    @DisplayName("Тесты на метод getDepartmentByName поиска отдела по имени")
    class GetDepartmentByNameTests {

        @ParameterizedTest
        @EnumSource(DepartmentNameEnum.class)
        @Tag("unit")
        @DisplayName("Тест с позитивным исходом")
        void getDepartmentByNameTest(DepartmentNameEnum name) {

            department.setName(name);

            when(departmentRepository.findDepartmentByName(name)).thenReturn(department);

            Department result = departmentService.getDepartmentByName(name.getStringConvert());

            assertEquals(result, department);
        }

        @ParameterizedTest
        @EnumSource(DepartmentNameEnum.class)
        @Tag("unit")
        @DisplayName("Тест на выброс DepartmentNotFoundException из-за неверного имени")
        void getDepartmentByNameNotFoundBadNameTest(DepartmentNameEnum name) {

            String badName = name.getStringConvert() + "я";
            department.setName(name);

            DepartmentNotFoundException exception = assertThrows(DepartmentNotFoundException.class,
                    () -> departmentService.getDepartmentByName(badName));

            assertEquals("Отдела '" + badName + "' не существует", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Тесты на метод saveDepartment сохранения отдела")
    class SaveDepartmentTests {

        @ParameterizedTest
        @EnumSource(DepartmentNameEnum.class)
        @Tag("unit")
        @DisplayName("Тест с позитивным исходом")
        void saveDepartmentTest(DepartmentNameEnum name) {

            createDepartmentRequest.setName(name.toString());
            department.setName(name);
            response.setName(name.getStringConvert());

            when(createDepartmentMapper.toEntity(createDepartmentRequest)).thenReturn(department);
            when(departmentRepository.existsDistinctByName(department.getName())).thenReturn(false);
            when(departmentRepository.save(department)).thenReturn(department);
            when(departmentRepository.findById(id)).thenReturn(Optional.ofNullable(department));
            when(findDepartmentMapper.entityToResponse(department)).thenReturn(response);

            DepartmentResponse result = departmentService.saveDepartment(createDepartmentRequest);

            assertEquals(result, response);

            verify(departmentRepository, times(1)).save(department);
            verify(createDepartmentMapper, times(1)).toEntity(createDepartmentRequest);
            verify(findDepartmentMapper, times(1)).entityToResponse(department);
        }

        @ParameterizedTest
        @EnumSource(DepartmentNameEnum.class)
        @Tag("unit")
        @DisplayName("Тест на выброс DepartmentNotFoundException")
        void saveDepartmentNotFoundTest(DepartmentNameEnum name) {

            createDepartmentRequest.setName(name.toString() + "z");

            DepartmentNotFoundException exception = assertThrows(
                    DepartmentNotFoundException.class,
                    () -> departmentService.saveDepartment(createDepartmentRequest));

            assertEquals("Отдела '" + createDepartmentRequest.getName() + "' не существует", exception.getMessage());

            verify(createDepartmentMapper, never()).toEntity(createDepartmentRequest);
            verify(departmentRepository,never()).existsDistinctByName(department.getName());
            verify(departmentRepository, never()).save(department);
            verify(findDepartmentMapper, never()).entityToResponse(department);
        }

        @ParameterizedTest
        @EnumSource(DepartmentNameEnum.class)
        @Tag("unit")
        @DisplayName("Тест на выброс PositionOccupiedException")
        void saveDepartmentPositionOccupiedTest(DepartmentNameEnum name) {

            createDepartmentRequest.setName(name.toString());
            department.setName(name);

            when(departmentRepository.existsDistinctByName(department.getName())).thenReturn(true);

            PositionOccupiedException exception = assertThrows(
                    PositionOccupiedException.class,
                    () -> departmentService.saveDepartment(createDepartmentRequest));

            assertEquals("Позиция '" + name + "' уже занята", exception.getMessage());

            verify(createDepartmentMapper, never()).toEntity(createDepartmentRequest);
            verify(departmentRepository,times(1)).existsDistinctByName(department.getName());
            verify(departmentRepository, never()).save(department);
            verify(findDepartmentMapper, never()).entityToResponse(department);
        }
    }

    @Nested
    @DisplayName("Тесты на метод updateDepartment обновления данных отдела")
    class UpdateDepartmentTests {

        @ParameterizedTest
        @CsvSource({
                "CIVIL_SERVICE_AND_HR, кадры",
                "FINANCE_AND_PLANNING, деньги",
                "LOGISTICS_SUPPORT, два степлера"
        })
        @Tag("unit")
        @DisplayName("Тест с позитивным исходом")
        void updateDepartmentTest(DepartmentNameEnum name, String description) {

            updateDepartmentRequest.setName(name.toString());
            updateDepartmentRequest.setDescription(description);
            response.setName(name.getStringConvert());
            response.setDescription(description);

            when(departmentRepository.findById(id)).thenReturn(Optional.of(department));
            doAnswer(invocation  -> {
                department.setName(name);
                department.setDescription(updateDepartmentRequest.getDescription());
                return null;
            }).when(updateDepartmentMapper).updateDepartmentData(updateDepartmentRequest,department);
            when(departmentRepository.existsDistinctByName(name)).thenReturn(false);
            when(departmentRepository.save(department)).thenReturn(department);
            when(findDepartmentMapper.entityToResponse(department)).thenReturn(response);

            DepartmentResponse result = departmentService.updateDepartment(id, updateDepartmentRequest);

            assertEquals(result, response);

            verify(departmentRepository, times(1)).findById(id);
            verify(updateDepartmentMapper, times(1)).updateDepartmentData(updateDepartmentRequest,department);
            verify(departmentRepository, times(1)).save(department);
            verify(findDepartmentMapper, times(1)).entityToResponse(department);
        }



        @Test
        @Tag("unit")
        @DisplayName("Тест на выброс IdNotFoundException")
        void updateDepartmentIdNotFoundTest() {

            when(departmentRepository.findById(badId)).thenReturn(Optional.empty());

            IdNotFoundException exception = assertThrows(IdNotFoundException.class,
                    () -> departmentService.updateDepartment(badId, updateDepartmentRequest));

            assertEquals("Id '" + badId + "' не найден", exception.getMessage());

            verify(departmentRepository, times(1)).findById(badId);
            verify(updateDepartmentMapper, never()).updateDepartmentData(updateDepartmentRequest,department);
            verify(departmentRepository, never()).existsDistinctByName(department.getName());
            verify(departmentRepository, never()).save(department);
            verify(findDepartmentMapper, never()).entityToResponse(department);
        }

        @ParameterizedTest
        @CsvSource({
                "CIVIL_SERVICE_AND_HR, кадры",
                "FINANCE_AND_PLANNING, деньги",
                "LOGISTICS_SUPPORT, два степлера",
                "LOGISTICS_SUPPORT, права"
        })
        @Tag("unit")
        @DisplayName("Тест на выброс DepartmentNotFoundException")
        void updateDepartmentNotFoundTest(DepartmentNameEnum name, String description) {

            updateDepartmentRequest.setName(name.toString() + "z");
            updateDepartmentRequest.setDescription(description);

            when(departmentRepository.findById(id)).thenReturn(Optional.of(department));
            doAnswer(invocation  -> {
                throw new DepartmentNotFoundException(updateDepartmentRequest.getName());
            }).when(updateDepartmentMapper).updateDepartmentData(updateDepartmentRequest,department);
            DepartmentNotFoundException exception = assertThrows(DepartmentNotFoundException.class,
                    () -> departmentService.updateDepartment(id, updateDepartmentRequest));

            assertEquals("Отдела '" + updateDepartmentRequest.getName() + "' не существует", exception.getMessage());

            verify(departmentRepository, times(1)).findById(id);
            verify(updateDepartmentMapper, times(1)).updateDepartmentData(updateDepartmentRequest,department);
            verify(departmentRepository, never()).existsDistinctByName(name);
            verify(departmentRepository, never()).save(department);
            verify(findDepartmentMapper, never()).entityToResponse(department);
        }


        @ParameterizedTest
        @CsvSource({
                "CIVIL_SERVICE_AND_HR, кадры",
                "FINANCE_AND_PLANNING, деньги",
                "LOGISTICS_SUPPORT, два степлера",
                "LOGISTICS_SUPPORT, права"
        })
        @Tag("unit")
        @DisplayName("Тест на выброс PositionOccupiedException")
        void updateDepartmentPositionOccupiedTest(DepartmentNameEnum name, String description) {

            updateDepartmentRequest.setName(name.getStringConvert());
            updateDepartmentRequest.setDescription(description);
            department.setName(name);

            when(departmentRepository.findById(id)).thenReturn(Optional.of(department));
            doAnswer(invocation  -> {
                department.setName(name);
                department.setDescription(updateDepartmentRequest.getDescription());
                return null;
            }).when(updateDepartmentMapper).updateDepartmentData(updateDepartmentRequest,department);
            when(departmentRepository.existsDistinctByName(department.getName())).thenReturn(true);

            PositionOccupiedException exception = assertThrows(PositionOccupiedException.class,
                    () -> departmentService.updateDepartment(id, updateDepartmentRequest));

            assertEquals("Позиция '" + name.getStringConvert() + "' уже занята", exception.getMessage());

            verify(departmentRepository, times(1)).findById(id);
            verify(updateDepartmentMapper, times(1)).updateDepartmentData(updateDepartmentRequest,department);
            verify(departmentRepository, times(1)).existsDistinctByName(name);
            verify(departmentRepository, never()).save(department);
            verify(findDepartmentMapper, never()).entityToResponse(department);
        }
    }

    @Nested
    @DisplayName("Тесты на метод deleteDepartment удаления отделя по id")
    class DeleteDepartmentByIdTests {

        @Test
        @Tag("unit")
        @DisplayName("Тест с позитивным исходом")
        void deleteDepartmentByIdTest() {
            when(departmentRepository.findById(id)).thenReturn(Optional.of(department));
            doAnswer(invocation -> {
                return null;}).when(departmentRepository).deleteById(id);

            departmentService.deleteDepartment(id);

            assertEquals(true, department.getEmps().isEmpty());

            verify(departmentRepository, times(1)).findById(id);
            verify(departmentRepository, times(1)).deleteById(id);
        }

        @Test
        @Tag("unit")
        @DisplayName("Тест на выброс IdNotFoundException")
        void deleteDepartmentIdNotFoundTest() {

            when(departmentRepository.findById(badId)).thenReturn(Optional.empty());

            IdNotFoundException exception = assertThrows(IdNotFoundException.class,
                    () -> departmentService.deleteDepartment(badId));

            assertEquals("Id '" + badId + "' не найден", exception.getMessage());

            verify(departmentRepository, times(1)).findById(badId);
            verify(departmentRepository, never()).deleteById(badId);
        }

        @Test
        @Tag("unit")
        @DisplayName("Тест на выброс DeleteDepartmentException")
        void deleteDepartmentByIdDeleteDepartmentExceptionTest() {
            department.setEmps(List.of(new Employee()));

            when(departmentRepository.findById(id)).thenReturn(Optional.of(department));

            DeleteDepartmentException exception = assertThrows(DeleteDepartmentException.class,
                    () -> departmentService.deleteDepartment(id));

            assertEquals(false, department.getEmps().isEmpty());
            assertEquals(department.getName().getStringConvert()+" имеет сотрудников", exception.getMessage());

            verify(departmentRepository, times(1)).findById(id);
            verify(departmentRepository, never()).deleteById(id);
        }
    }
}
