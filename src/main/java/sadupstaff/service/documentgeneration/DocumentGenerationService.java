package sadupstaff.service.documentgeneration;

import sadupstaff.dto.request.generationdocument.DocumentJobRegulationRequest;

public interface DocumentGenerationService {

    void generateDocumentJobRegulationSecretarySession(String sectionPersonelNumber, DocumentJobRegulationRequest request);
}
