package sadupstaff.controller.documentgeneration;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import sadupstaff.dto.request.generationdocument.DocumentJobRegulationRequest;
import sadupstaff.service.documentgeneration.DocumentGenerationService;

@RestController
@RequiredArgsConstructor
public class DocumentGenerationControllerImpl implements DocumentGenerationController {

    private final DocumentGenerationService documentGenerationService;

    @Override
    public void jobRegulationSecretarySession(String sectionPersonelNumber, DocumentJobRegulationRequest request) {

    }
}
