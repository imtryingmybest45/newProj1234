package com.example.demo;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.io.IOException;

@RestController
public class SecondaryController {
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:3000","https://delightful-mushroom-0b98f760f.3.azurestaticapps.net/"})
    @PostMapping("/genericEndpoint123")
    public String getData(@RequestBody String content) {
        String fileStuff;
        String urlName1 = content.replace("%2F", "");
        String urlName2 = urlName1.replace("=", "");
        String fileName = "C:\\Mac\\Home\\Documents\\SampleFolder\\"+urlName2+".txt"; // Replace with your file name
        Path filePath = Paths.get(fileName);
        try {
            fileStuff = Files.readString(filePath, StandardCharsets.UTF_8);
        }catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            fileStuff = "Hello World! I have errored. Again";
        }
        return fileStuff;
    }
}