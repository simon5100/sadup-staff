package sadupstaff.service.documentgeneration;

import org.springframework.http.HttpHeaders;
import sadupstaff.dto.request.generationdocument.DocumentJobRegulationRequest;
import java.util.HashMap;

public interface DocumentGenerationService {

    HashMap<HttpHeaders, byte[]> generateDocumentJobRegulationSecretarySession(DocumentJobRegulationRequest request);
}
