package sadupstaff.service.documentgeneration;

import sadupstaff.dto.request.generationdocument.DocumentJobRegulationRequest;

public interface DocumentGenerationService {

    byte[] generateDocumentJobRegulationSecretarySession(String sectionPersonelNumber, DocumentJobRegulationRequest request);
}
