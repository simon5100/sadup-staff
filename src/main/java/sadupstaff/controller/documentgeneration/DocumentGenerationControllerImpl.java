package sadupstaff.controller.documentgeneration;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RestController;
import sadupstaff.dto.request.generationdocument.DocumentJobRegulationRequest;
import sadupstaff.service.documentgeneration.DocumentGenerationService;

import java.util.HashMap;

@Log4j2
@RestController
@RequiredArgsConstructor
public class DocumentGenerationControllerImpl implements DocumentGenerationController {

    private final DocumentGenerationService documentGenerationService;

    @Override
    public ResponseEntity<byte[]> jobRegulationSecretarySession(DocumentJobRegulationRequest request) {

        HashMap<HttpHeaders, byte[]> documentContainer = documentGenerationService.generateDocumentJobRegulationSecretarySession(request);

        HttpHeaders headers = documentContainer.keySet().stream().findFirst().get();

        byte[] document = documentContainer.get(documentContainer.keySet().stream().findFirst().get());

        return new ResponseEntity<>(document, headers, HttpStatus.OK);
    }
}