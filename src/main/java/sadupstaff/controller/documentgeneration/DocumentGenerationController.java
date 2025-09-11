package sadupstaff.controller.documentgeneration;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import sadupstaff.dto.request.generationdocument.DocumentJobRegulationRequest;

@RequestMapping("/api")
public interface DocumentGenerationController {

    @PostMapping("/v1/document/generation/jobRegulation/secretarySession/{sectionPersonelNumber}")
    ResponseEntity<byte[]> jobRegulationSecretarySession(@PathVariable String sectionPersonelNumber, @RequestBody DocumentJobRegulationRequest request);
}