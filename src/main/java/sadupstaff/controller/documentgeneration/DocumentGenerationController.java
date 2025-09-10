package sadupstaff.controller.documentgeneration;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api")
public interface DocumentGenerationController {

    @GetMapping("/v1/{sectionPersonelNumber}")
    void jobRegulationSecretarySession();
}