package com.example.demo;

import org.kohsuke.github.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

@RestController
public class DeleteController {
    @CrossOrigin(origins = {"http://localhost:3000",
            "https://delightful-mushroom-0b98f760f.3.azurestaticapps.net/",
            "https://www.aprilshorrorcorner.com",
            "https://aprilshorrorcorner.com",
            "https://green-smoke-0fa35931e.6.azurestaticapps.net/"})

    @PostMapping("/deleteEndpoint")

    public String deleteData(@RequestBody String movieName) throws IOException {

        OtherFunctions otherFunctions = new OtherFunctions();
        Constants constants = new Constants();

        movieName = movieName.substring(0, movieName.length() - 1);
        String movieNameWithSpaces = movieName.replace("+", " ");
        String movieNameWithoutSpaces = movieNameWithSpaces.replace(" ", "");

        String repoOwner = constants.repoOwner;
        String repoName = constants.repoName;
        String gitToken = constants.gitToken;
        String branch = constants.branch;
        String filePath = "src/pages/Home.js";

        GitHub github = new GitHubBuilder().withOAuthToken(gitToken).build();
        GHRepository repo = github.getUser(repoOwner).getRepository(repoName);

        GHContent stuff = repo.getFileContent(filePath, branch);
        String origFileCont = new String(stuff.read().readAllBytes(), "UTF-8");

        String newFileCont = otherFunctions.removeFile(movieNameWithSpaces, origFileCont);

        repo.createContent()
                .path(filePath)
                .content(newFileCont.getBytes("UTF-8"))
                .message("Updated file via Java API")
                .sha(stuff.getSha()) // Important for optimistic locking
                .branch(branch)
                .commit();

        String filePath2 = "src/pages/" + movieNameWithoutSpaces + ".js";
        Timer timer = new Timer(); // Create a Timer object
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    GitHub githubDelete = new GitHubBuilder().withOAuthToken(gitToken).build();
                    GHRepository repoDelete = githubDelete.getUser(repoOwner).getRepository(repoName);
                    GHContent deletedFile = repoDelete.getFileContent(filePath2, branch);
                    deletedFile.delete("Deleted file via Java API");
                } catch (IOException e) {
                    System.err.println("An error occurred: " + e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        };
        timer.schedule(task,constants.timeDelay);
        return "Successfully deleted "+movieNameWithSpaces+". Please wait a few moments for the website to refresh.";
    }
}
