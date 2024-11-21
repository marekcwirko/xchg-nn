package org.example.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WebController {

    @GetMapping("/api/health")
    public ResponseEntity<String> helthy() {
        return ResponseEntity.ok("OK");
    }
}