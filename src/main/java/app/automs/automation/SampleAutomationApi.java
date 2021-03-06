package app.automs.automation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Defines a controller to handle HTTP requests
 */
@RestController
@RequestMapping("/api/automation/run/sefaz-go-pendencia")
public class SampleAutomationApi {
    @Autowired
    private SampleAutomation sampleAutomation;

    @GetMapping(value = "/{cnpj}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> runAutomation(@PathVariable String cnpj) {
        String[] args = {cnpj};
        String response = sampleAutomation.run(args);
        return ResponseEntity.ok(response);
    }
}
