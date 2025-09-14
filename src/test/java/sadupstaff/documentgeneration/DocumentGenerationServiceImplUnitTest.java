//package sadupstaff.documentgeneration;
//
//import mockwebserver3.MockResponse;
//import mockwebserver3.MockWebServer;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.HttpStatusCode;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
//import org.springframework.web.reactive.function.client.ClientResponse;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//import sadupstaff.dto.request.generationdocument.DocumentJobRegulationRequest;
//import sadupstaff.dto.response.DocumentJobRegulationResponse;
//import sadupstaff.entity.district.District;
//import sadupstaff.entity.district.Section;
//import sadupstaff.entity.district.SectionEmployee;
//import sadupstaff.enums.DistrictName;
//import sadupstaff.exception.ErrorResponse;
//import sadupstaff.exception.documentgeneration.DocumentGenerationException;
//import sadupstaff.repository.SectionRepository;
//import sadupstaff.service.documentgeneration.DocumentGenerationServiceImpl;
//import sadupstaff.service.section.SectionService;
//
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("Unit тесты методов DocumentGenerationServiceImpl")
//public class DocumentGenerationServiceImplUnitTest {
//
//    private MockWebServer mockWebServer;
//
//    @Mock
//    SectionService sectionService;
//
//
//    private WebClient webClient;
//
//    @Mock
//    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
//
//    @Mock
//    private WebClient.RequestBodySpec requestBodySpec;
//
//    @Mock
//    private WebClient.RequestHeadersSpec<?> requestHeadersSpec;
//
//    @Mock
//    private WebClient.ResponseSpec responseSpec;
//
//    @InjectMocks
//    private DocumentGenerationServiceImpl documentGenerationService;
//
//    String sectionPersonelNumber;
//
//    String badSectionPersonelNumber;
//
//    DocumentJobRegulationRequest request;
//
//    DocumentJobRegulationResponse response;
//
//    String url;
//
//    Section section;
//
//
//
//    @BeforeEach
//    void setUp() throws IOException {
//        url = "http://localhost:8081/api/documents/v1/generation/jobRegulation/secretarySession";
//
//        mockWebServer = new MockWebServer();
//        mockWebServer.start();
//
//        webClient = WebClient.builder()
//                .baseUrl(mockWebServer.url(url).toString())
//                .build();
//
//        sectionPersonelNumber = "M540000";
//        badSectionPersonelNumber = "M5400000";
//
//        request = new DocumentJobRegulationRequest(
//                "Ф.М. Ивановна",
//                false,
//                "Т.А. Сергеевич",
//                false,
//                "З.Е. Викторовна"
//        );
//
//        response = new DocumentJobRegulationResponse(
//                "Железнодорожный",
//                1,
//                request.getJudgeName(),
//                request.getActingJudge(),
//                request.getJudgeOrganizerName(),
//                request.getActingJudgeOrganizer(),
//                request.getConcordantName()
//        );
//
//
//        District district = new District(
//                UUID.randomUUID(),
//                DistrictName.ZHELEZNODOROZHHNY,
//                "asldfj",
//                4,
//                LocalDateTime.now(),
//                LocalDateTime.now(),
//                List.of(new Section())
//        );
//
//        section = new Section(
//                UUID.randomUUID(),
//                sectionPersonelNumber,
//                1,
//                4,
//                LocalDateTime.now(),
//                LocalDateTime.now(),
//                district,
//                List.of(new SectionEmployee())
//        );
//
//
//    }
//
//
//    @Nested
//    @DisplayName("Тест с позитивным исходом")
//    class GenerateDocumentJobRegulationSecretarySessionTest{
//
//        @Test
//        @Tag("unit")
//        @DisplayName("Тест с позитивным исходом")
//        void generateDocumentJobRegulationSecretarySessionTest(){
//
//            mockWebServer.enqueue(new MockResponse()
//                    .setResponseCode(200)
//                    .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
//                    .setBody(new byte[0]));
//
//            when(sectionService.getSectionByPersonelNumber(sectionPersonelNumber)).thenReturn(section);
//
//
//
//
//
//
//            byte[] result = documentGenerationService.generateDocumentJobRegulationSecretarySession(
//                    sectionPersonelNumber, request
//            );
//
//            assertNotNull(result);
//
//            verify(sectionService, times(1)).getSectionByPersonelNumber(sectionPersonelNumber);
//        }
//    }
//
//
//}