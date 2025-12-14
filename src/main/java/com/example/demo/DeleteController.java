package com.example.demo;

import org.kohsuke.github.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class DeleteController {
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:3000","https://delightful-mushroom-0b98f760f.3.azurestaticapps.net/","https://www.aprilshorrorcorner.com","https://aprilshorrorcorner.com"})
    @PostMapping("/deleteEndpoint")
    public String deleteData(@RequestBody String movieName) throws IOException {
        OtherFunctions otherFunctions = new OtherFunctions();
        String repoName = "frontEndAppCode";
        String gitToken = System.getenv("HEDGEHOG");
        String owner = "imtryingmybest45";
        String filePath = "src/pages/Home.js";
        String branch = "main"; // Or your target branch

        GitHub github = new GitHubBuilder().withOAuthToken(gitToken).build();
        GHRepository repo = github.getUser(owner).getRepository(repoName);

        GHContent stuff = repo.getFileContent(filePath, branch);
        String origFileCont = new String(stuff.read().readAllBytes(), "UTF-8");

        String newFileCont = otherFunctions.removeFile(movieName, origFileCont);
        repo.createContent()
                .path(filePath)
                .content(newFileCont.getBytes("UTF-8"))
                .message("Deleted file via Java API")
                .sha(stuff.getSha()) // Important for optimistic locking
                .branch(branch)
                .commit();

        return "Successfully deleted "+movieName+". Please wait a few moments for the website to refresh.";
    }
}
