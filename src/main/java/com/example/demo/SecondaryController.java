package com.example.demo;

import org.kohsuke.github.*;
import org.springframework.web.bind.annotation.*;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.nio.file.StandardOpenOption;

@RestController
public class SecondaryController {
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:3000","https://delightful-mushroom-0b98f760f.3.azurestaticapps.net/"})
    @PostMapping("/genericEndpoint123")
    public String getData(@RequestBody MyRequestDTO requestDTO) throws IOException {

        //adding new line
        String content = requestDTO.getMovieName();
        String movieReview = requestDTO.getMovieReview();

        String nextString = content.substring(0, content.length());
        String newString = nextString.replaceAll("\\s", "");

        OtherFunctions otherFunctions = new OtherFunctions();
        String name = newString;
        String repoName = "frontEndCode";
        String gitToken = System.getenv("MY_AWESOME_PAT");

        otherFunctions.writeNewPagesFile(name,repoName,gitToken,movieReview);
        String origName = otherFunctions.getOrigName(repoName,gitToken);
        String newContent = otherFunctions.editRoutesAppFile(name,repoName,gitToken);
        String newContent2 = otherFunctions.editLinksAppFile(name, origName, newContent);
        String newContent3 = otherFunctions.addImportLine(name, newContent2);

        String githubToken = gitToken; // Replace with your token
        String owner = "imtryingmybest45";
        String filePath = "src/pages/Home.js";
        String branch = "main"; // Or your target branch

        GitHub github = new GitHubBuilder().withOAuthToken(githubToken).build();
        GHRepository repo = github.getUser(owner).getRepository(repoName);

        GHContent stuff = repo.getFileContent(filePath, branch);
        String currentContent = new String(stuff.read().readAllBytes(), "UTF-8");

        repo.createContent()
                .path(filePath)
                .content(newContent3.getBytes("UTF-8"))
                .message("Updated file via Java API")
                .sha(stuff.getSha()) // Important for optimistic locking
                .branch(branch)
                .commit();

        return "You have submitted your review. Please wait a few minutes for the website to refresh.";
    }
}