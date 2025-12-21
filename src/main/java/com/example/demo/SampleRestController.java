package com.example.demo;

import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins="http://localhost:3000")
public class SampleRestController {
    @GetMapping("/genericEndpoint")
    public String genericEndpoint() {
        return "Hello World123";
    }
}
