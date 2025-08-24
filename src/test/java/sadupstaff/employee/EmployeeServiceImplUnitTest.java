package sadupstaff.employee;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sadupstaff.mapper.employee.CreateEmployeeMapper;
import sadupstaff.mapper.employee.FindEmployeeMapper;
import sadupstaff.mapper.employee.UpdateEmployeeMapper;
import sadupstaff.repository.EmployeeRepository;
import sadupstaff.service.department.DepartmentServiceImpl;
import sadupstaff.service.employee.EmployeeServiceImpl;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit тесты методов EmployeeServiceImpl")
public class EmployeeServiceImplUnitTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentServiceImpl departmentService;

    @Mock
    private UpdateEmployeeMapper updateEmployeeMapper;

    @Mock
    private FindEmployeeMapper findEmployeeMapper;

    @Mock
    private CreateEmployeeMapper createEmployeeMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private UUID id;
    private UUID badId;


}
