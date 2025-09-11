package sadupstaff.controller.documentgeneration;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RestController;
import sadupstaff.dto.request.generationdocument.DocumentJobRegulationRequest;
import sadupstaff.service.documentgeneration.DocumentGenerationService;

@RestController
@RequiredArgsConstructor
public class DocumentGenerationControllerImpl implements DocumentGenerationController {

    private final DocumentGenerationService documentGenerationService;

    @Override
    public ResponseEntity<byte[]> jobRegulationSecretarySession(String sectionPersonelNumber, DocumentJobRegulationRequest request) {

        byte[] bytes = documentGenerationService.generateDocumentJobRegulationSecretarySession(sectionPersonelNumber, request);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
                ContentDisposition.inline()
                        .filename("document.pdf")
                        .build());
        headers.setContentLength(bytes.length);

        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }
}
