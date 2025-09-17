package sadupstaff.service.documentgeneration;

import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class DocumentGenerationServiceImpl implements DocumentGenerationService {

    private final SectionService sectionService;
    private final Pattern names = Pattern.compile("[А-Я]\\.[А-Я]\\. [А-Я][а-я]*");
    private final Pattern sectionPersonalNumbers = Pattern.compile("54MS0[0-1]\\d{2}");

    private HttpHeaders headers;
    private byte[] document;
    private Section section;
    DocumentJobRegulationResponse response;

    @Override
    public HashMap<HttpHeaders, byte[]> generateDocumentJobRegulationSecretarySession(DocumentJobRegulationRequest request) {

        if (!sectionPersonalNumbers.matcher(request.getSectionPersonalNumber()).find()) {
            throw new RuntimeException("неверный формат номера");
        } else if (!(names.matcher(request.getJudgeName()).find() &&
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

        String url = "http://localhost:8081/api/documents/v1/generation/jobRegulation/secretarySession";

        document = WebClient.builder()
                .build()
                .post()
                .uri("${document-generation.urls.url-jobRegulation-secretarySession}")
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
                                "uchastok № %d.pdf",
                        section.getDistrict().getName(), section.getNumber()))
                .build());
        headers.setContentLength(document.length);

        return (HashMap<HttpHeaders, byte[]>) new HashMap<>().put(headers, document);
    }
}