package sadupstaff.service.documentgeneration;

import lombok.RequiredArgsConstructor;
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
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class DocumentGenerationServiceImpl implements DocumentGenerationService {

    private final SectionService sectionService;

    @Override
    public byte[] generateDocumentJobRegulationSecretarySession(String sectionPersonelNumber, DocumentJobRegulationRequest request) {

        Section section = sectionService.getSectionByPersonelNumber(sectionPersonelNumber);

        Pattern pattern = Pattern.compile("[А-Я]\\.[А-Я]\\. [А-Я][а-я]*");

        if (!(pattern.matcher(request.getJudgeName()).find() &&
                pattern.matcher(request.getJudgeOrganizerName()).find() &&
                pattern.matcher(request.getConcordantName()).find())) {
            throw new IncorrectNAMEFormatException(
                    request.getJudgeName(),
                    request.getJudgeOrganizerName(),
                    request.getConcordantName());
        }

        DocumentJobRegulationResponse response = new DocumentJobRegulationResponse(
                section.getDistrict().getName().getStringConvert(),
                section.getNumber(),
                request.getJudgeName(),
                request.getActingJudge(),
                request.getJudgeOrganizerName(),
                request.getActingJudgeOrganizer(),
                request.getConcordantName()
        );

        String url = "http://localhost:8081/api/documents/v1/generation/jobRegulation/secretarySession";

        return WebClient.builder()
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
    }
}