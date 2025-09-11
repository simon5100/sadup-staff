package sadupstaff.service.documentgeneration;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sadupstaff.dto.request.generationdocument.DocumentJobRegulationRequest;
import sadupstaff.dto.response.DocumentJobRegulationResponse;
import sadupstaff.entity.district.Section;
import sadupstaff.repository.SectionRepository;

import java.net.URL;

@Service
@RequiredArgsConstructor
public class DocumentGenerationServiceImpl implements DocumentGenerationService {

    private final SectionRepository sectionRepository;

    @Override
    public void generateDocumentJobRegulationSecretarySession(String sectionPersonelNumber, DocumentJobRegulationRequest request) {

        Section section = sectionRepository.findSectionByPersonelNumber(sectionPersonelNumber);

        DocumentJobRegulationResponse response = new DocumentJobRegulationResponse(
                section.getDistrict().getName().getStringConvert(),
                section.getNumber(),
                request.getJudgeName(),
                request.getActingJudge(),
                request.getJudgeOrganizerName(),
                request.getActingJudgeOrganizer(),
                request.getConcordantName()
        );

        String url = "";


    }
}
