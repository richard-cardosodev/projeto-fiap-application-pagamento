package br.fiap.projeto.pagamento.external.configuration;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LivenessResource {

    @GetMapping("/liveness")
    public ResponseEntity<String> livenessProbe() {

        return ResponseEntity.ok().body("LIVE");
    }
}
