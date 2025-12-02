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
import com.example.demo.FTPStuff;

@RestController
public class TertiaryController {
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:3000","https://delightful-mushroom-0b98f760f.3.azurestaticapps.net/"})
    @PostMapping("/ftpEndpoint")
    public String getData(@RequestBody String content) {
        String fileStuff;
        String urlName1 = content.replace("%2F", "");
        String urlName2 = urlName1.replace("=", "");
        try {
            FTPStuff ftpStuff = new FTPStuff();
            fileStuff = ftpStuff.getFTPClient(urlName2);
        }catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            fileStuff = "Hello World! I have errored. Again. and again,sadly";
        }
        return fileStuff;
    }
}
