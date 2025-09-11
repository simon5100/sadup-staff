package sadupstaff.controller.documentgeneration;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import sadupstaff.dto.request.generationdocument.DocumentJobRegulationRequest;

@RequestMapping("/api")
public interface DocumentGenerationController {

    @PostMapping("/v1/{sectionPersonelNumber}")
    void jobRegulationSecretarySession(@PathVariable String sectionPersonelNumber, @RequestBody DocumentJobRegulationRequest request);
}