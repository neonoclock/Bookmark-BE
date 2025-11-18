package com.example.ktbapi.common;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDateTime;

@RestController
public class HomeController {

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object root() {
        return new RedirectView("/swagger-ui/index.html");
    }

    @GetMapping("/health")
    public Msg health() {
        return new Msg("OK", LocalDateTime.now().toString());
    }
}
