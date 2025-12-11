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
import java.util.Timer;
import java.util.TimerTask;

@RestController
public class SecondaryController {
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:3000","https://delightful-mushroom-0b98f760f.3.azurestaticapps.net/","https://www.aprilshorrorcorner.com","https://aprilshorrorcorner.com"})
    @PostMapping("/genericEndpoint123")
    public String getData(@RequestBody MyRequestDTO requestDTO) throws IOException {

        String content = requestDTO.getMovieName();
        String movieReview = requestDTO.getMovieReview();

        String nextString = content.substring(0, content.length());
        String newString = nextString.replaceAll("\\s", "");

        OtherFunctions otherFunctions = new OtherFunctions();
        String name = newString;
        String repoName = "frontEndAppCode";
        //String gitToken = System.getenv("MY_AWESOME_PAT");
        String gitToken = System.getenv("HEDGEHOG");
        System.out.println("Git Token: " + gitToken);

        otherFunctions.writeNewPagesFile(nextString, name,repoName,gitToken,movieReview);
        Timer timer = new Timer(); // Create a Timer object
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                String origName = null;
                try {
                    origName = otherFunctions.getOrigName(repoName,gitToken);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                String newContent = null;
                try {
                    newContent = otherFunctions.editRoutesAppFile(name,repoName,gitToken);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                String newContent2 = null;
                try {
                    newContent2 = otherFunctions.editLinksAppFile(name, origName, newContent, nextString);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                String newContent3 = null;
                try {
                    newContent3 = otherFunctions.addImportLine(name, newContent2);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                //String githubToken = gitToken; // Replace with your token
                String owner = "imtryingmybest45";
                String filePath = "src/pages/Home.js";
                String branch = "main"; // Or your target branch

                GitHub github2 = null;
                try {
                    github2 = new GitHubBuilder().withOAuthToken(gitToken).build();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                GHRepository repo2 = null;
                try {
                    repo2 = github2.getUser(owner).getRepository(repoName);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                GHContent stuff = null;
                try {
                    stuff = repo2.getFileContent(filePath, branch);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    String currentContent = new String(stuff.read().readAllBytes(), "UTF-8");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                try {
                    repo2.createContent()
                            .path(filePath)
                            .content(newContent3.getBytes("UTF-8"))
                            .message("Updated file via Java API")
                            .sha(stuff.getSha()) // Important for optimistic locking
                            .branch(branch)
                            .commit();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                //repo.createContent(newContent3, "heello", filePath);

            }
        };
        /*String origName = otherFunctions.getOrigName(repoName,gitToken);
        String newContent = otherFunctions.editRoutesAppFile(name,repoName,gitToken);
        String newContent2 = otherFunctions.editLinksAppFile(name, origName, newContent);
        String newContent3 = otherFunctions.addImportLine(name, newContent2);

        //String githubToken = gitToken; // Replace with your token
        String owner = "imtryingmybest45";
        String filePath = "src/pages/Home.js";
        String branch = "main"; // Or your target branch

        GitHub github2 = new GitHubBuilder().withOAuthToken(gitToken).build();
        GHRepository repo2 = github2.getUser(owner).getRepository(repoName);

        GHContent stuff = repo2.getFileContent(filePath, branch);
        String currentContent = new String(stuff.read().readAllBytes(), "UTF-8");

        repo2.createContent()
                .path(filePath)
                .content(newContent3.getBytes("UTF-8"))
                .message("Updated file via Java API")
                .sha(stuff.getSha()) // Important for optimistic locking
                .branch(branch)
                .commit();

        //repo.createContent(newContent3, "heello", filePath);

        return "You have submitted your review. Please wait a few minutes for the website to refresh.";*/
        //timer.schedule(task, 300000);
        timer.schedule(task,150000);
        return "You have submitted your review. Please wait a few minutes for the website to refresh.";

    }
}
