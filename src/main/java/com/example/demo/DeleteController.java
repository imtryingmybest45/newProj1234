package com.example.demo;

import org.kohsuke.github.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DeleteController {
    @CrossOrigin(origins = {"http://localhost:3000",
            "https://green-smoke-0fa35931e.6.azurestaticapps.net/",
            "https://www.aprilshorrorcorner.com",
            "https://aprilshorrorcorner.com",
            "https://zealous-desert-09313150f.6.azurestaticapps.net/"})

    @PostMapping("/deleteEndpoint")

    public String deleteData(@RequestBody String movieName) throws IOException {

        OtherFunctions otherFunctions = new OtherFunctions();
        Constants constants = new Constants();

        movieName = movieName.substring(0, movieName.length() - 1);
        String movieNameWithSpaces = otherFunctions.decodeMovieURL(movieName);
        String movieNameWithoutSpaces = movieNameWithSpaces.replace(" ", "");
        movieNameWithoutSpaces = movieNameWithoutSpaces.replaceAll("[^a-zA-Z0-9]", "");

        String repoOwner = constants.repoOwner;
        String repoName = constants.repoName;
        String gitToken = constants.gitToken;
        String branch = constants.branch;
        String filePath = "src/pages/Home.js";

        GitHub github = new GitHubBuilder().withOAuthToken(gitToken).build();
        GHRepository repo = github.getUser(repoOwner).getRepository(repoName);

        GHContent stuff = repo.getFileContent(filePath, branch);
        String origFileCont = new String(stuff.read().readAllBytes(), "UTF-8");

        String newPagesFileContent = otherFunctions.removeFile(movieNameWithoutSpaces, origFileCont);

        Map<String, String> filesContent = new HashMap<>();
        // Adding items
        filesContent.put("src/pages/Home.js", newPagesFileContent);

        otherFunctions.commitDeletedFiles(filesContent, gitToken, repoOwner, repoName, branch, movieNameWithoutSpaces);
        //otherFunctions.commitDeletedFiles(filesContent, gitToken, repoOwner, repoName, branch, movNameWithoutSpaces);

        return "You have successfully deleted "+movieNameWithSpaces+". Please wait a few minutes for the website to refresh.";
    }
}
