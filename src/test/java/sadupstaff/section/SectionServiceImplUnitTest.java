package sadupstaff.section;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sadupstaff.mapper.section.CreateSectionMapper;
import sadupstaff.mapper.section.FindSectionMapper;
import sadupstaff.mapper.section.UpdateSectionMapper;
import sadupstaff.repository.SectionRepository;
import sadupstaff.service.district.DistrictServiceImpl;
import sadupstaff.service.section.SectionServiceImpl;
import java.util.UUID;

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

    private UUID id;
    private UUID badId;

}
