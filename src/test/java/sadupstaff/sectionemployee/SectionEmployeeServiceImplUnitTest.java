package sadupstaff.sectionemployee;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sadupstaff.mapper.sectionemployee.CreateSectionEmployeeMapper;
import sadupstaff.mapper.sectionemployee.FindSectionEmployeeMapper;
import sadupstaff.mapper.sectionemployee.UpdateSectionEmployeeMapper;
import sadupstaff.repository.SectionEmployeeRepository;
import sadupstaff.service.section.SectionServiceImpl;
import sadupstaff.service.sectionemployee.SectionEmployeeServiceImpl;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit тесты методов SectionEmployeeServiceImpl")
public class SectionEmployeeServiceImplUnitTest {

    @Mock
    private UpdateSectionEmployeeMapper updateSectionEmployeeMapper;

    @Mock
    private SectionEmployeeRepository sectionEmployeeRepository;

    @Mock
    private SectionServiceImpl sectionService;

    @Mock
    private FindSectionEmployeeMapper findSectionEmployeeMapper;

    @Mock
    private CreateSectionEmployeeMapper createSectionEmployeeMapper;

    @InjectMocks
    private SectionEmployeeServiceImpl sectionEmployeeService;

    private UUID id;
    private UUID badId;



}
