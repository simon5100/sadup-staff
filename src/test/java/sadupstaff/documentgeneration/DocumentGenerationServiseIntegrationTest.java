package sadupstaff.documentgeneration;

import lombok.extern.log4j.Log4j2;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import sadupstaff.dto.request.generationdocument.DocumentJobRegulationRequest;
import sadupstaff.dto.response.DepartmentResponse;
import sadupstaff.dto.response.DocumentJobRegulationResponse;
import sadupstaff.entity.district.Section;
import sadupstaff.entity.management.Department;
import sadupstaff.exception.documentgeneration.IncorrectNAMEFormatException;
import sadupstaff.service.documentgeneration.DocumentGenerationService;
import sadupstaff.service.section.SectionService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpHeaders.*;

@Log4j2
@SpringBootTest
@Testcontainers
@Transactional
@TestPropertySource(properties = {
        "document-generation.urls.url-jobRegulation-secretarySession=http://localhost:8089/mock-document-generation"
})
@DisplayName("Integration тесты методов DepartmentServiceImpl")
public class DocumentGenerationServiseIntegrationTest {

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
    private SectionService sectionService;

    @Autowired
    private DocumentGenerationService documentGenerationService;

    private MockWebServer mockWebServer;


    private DocumentJobRegulationRequest request;

    private DocumentJobRegulationResponse response;

    byte[] mockPdf  = "document".getBytes();

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start(8089);

        request = new DocumentJobRegulationRequest(
                "54MS0010",
                "Ф.М. Ивановна",
                false,
                "Т.А. Сергеевич",
                false,
                "З.Е. Викторовна"
        );

        response = new DocumentJobRegulationResponse(
                "Железнодорожный",
                2,
                "Ф.М. Ивановна",
                false,
                "Т.А. Сергеевич",
                false,
                "З.Е. Викторовна"
        );
    }

    @AfterEach
    void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Nested
    @Transactional
    @DisplayName("Тесты на метод generateDocumentJobRegulationSecretarySession")
    class GenerateDocumentJobRegulationSecretarySessionTests {

        @Test
        @Tag("integration")
        @DisplayName("Тест с позитивным исходом")
        void generateDocumentJobRegulationSecretarySessionTest() {

            mockWebServer.enqueue(new MockResponse()
                    .setBody(new String(mockPdf))
                    .setHeader(CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                    .setResponseCode(200));


            HashMap<HttpHeaders, byte[]> result = documentGenerationService.generateDocumentJobRegulationSecretarySession(request);

            assertNotNull(result);
            assertEquals(1, result.size());

            HttpHeaders headers = result.keySet().iterator().next();
            byte[] document = result.get(headers);

            assertNotNull(document);
            assertEquals("document", new String(document));
            assertEquals(MediaType.APPLICATION_PDF, headers.getContentType());
        }

        @Test
        @Tag("integration")
        @DisplayName("Тест на выброс IncorrectNAMEFormatException")
        void generateDocumentJobRegulationSecretarySessionIncorrectNAMEFormatExceptionTest() {

            request.setConcordantName("Иванов Иван Иванович");

            mockWebServer.enqueue(new MockResponse()
                    .setBody(new String(mockPdf))
                    .setHeader(CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                    .setResponseCode(200));

            IncorrectNAMEFormatException exception = assertThrows(IncorrectNAMEFormatException.class,
                    () -> documentGenerationService.generateDocumentJobRegulationSecretarySession(request));

            assertNotNull(exception);
            assertEquals(exception.getMessage(), String.format(
                    "Cудья: %s; " +
                            "Судья организатор: %s; " +
                            "Сотрудник согласующего отдела: %s; " +
                            "У судьи, судьи организотора или сотрудника согласующего отдела неверный формат ФИО " +
                            "Пример верного формата: И.И. Иванов",
                    request.getJudgeName(), request.getJudgeOrganizerName(), request.getConcordantName()));
        }
    }
}
