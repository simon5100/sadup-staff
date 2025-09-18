package sadupstaff.service.documentgeneration;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import sadupstaff.dto.request.generationdocument.DocumentJobRegulationRequest;
import sadupstaff.dto.response.DocumentJobRegulationResponse;
import sadupstaff.entity.district.Section;
import sadupstaff.exception.documentgeneration.DocumentGenerationException;
import sadupstaff.exception.documentgeneration.IncorrectNAMEFormatException;
import sadupstaff.service.section.SectionService;
import java.util.HashMap;
import java.util.regex.Pattern;

@Log4j2
@Service
@RequiredArgsConstructor
public class DocumentGenerationServiceImpl implements DocumentGenerationService {

    private final SectionService sectionService;
    private final Pattern names = Pattern.compile("[А-Я]\\.[А-Я]\\. [А-Я][а-я]*");
    @Value("${document-generation.urls.url-jobRegulation-secretarySession}")
    private String url;

    private HttpHeaders headers;
    private byte[] document;
    private Section section;
    private DocumentJobRegulationResponse response;
    private HashMap<HttpHeaders, byte[]> documentContainer;


    @Override
    public HashMap<HttpHeaders, byte[]> generateDocumentJobRegulationSecretarySession(DocumentJobRegulationRequest request) {

        if (!(names.matcher(request.getJudgeName()).find() &&
                names.matcher(request.getJudgeOrganizerName()).find() &&
                names.matcher(request.getConcordantName()).find())) {
            throw new IncorrectNAMEFormatException(
                    request.getJudgeName(),
                    request.getJudgeOrganizerName(),
                    request.getConcordantName());
        }

        section = sectionService.getSectionByPersonelNumber(request.getSectionPersonalNumber());

        response = new DocumentJobRegulationResponse(
                section.getDistrict().getName().getStringConvert(),
                section.getNumber(),
                request.getJudgeName(),
                request.getActingJudge(),
                request.getJudgeOrganizerName(),
                request.getActingJudgeOrganizer(),
                request.getConcordantName()
        );

        document = WebClient.builder()
                .build()
                .post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(response)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> {
                    throw new DocumentGenerationException();
                })
                .bodyToMono(byte[].class)
                .block();

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.inline()
                .filename(String.format(
                        "Dolzhnostnoy reglament SSZ " +
                                "rayon %s " +
                                "uchastok nomer %d.pdf",
                        section.getDistrict().getName(), section.getNumber()))
                .build());
        headers.setContentLength(document.length);

        documentContainer = new HashMap<>();
        documentContainer.put(headers, document);

        return documentContainer;
    }
}