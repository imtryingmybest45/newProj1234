package com.example.demo;

import org.kohsuke.github.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@RestController
public class EditController {
    @CrossOrigin(origins = {"http://localhost:3000",
            "https://delightful-mushroom-0b98f760f.3.azurestaticapps.net/",
            "https://www.aprilshorrorcorner.com",
            "https://aprilshorrorcorner.com"})

    @PostMapping("/editEndpoint")

    public String getData(@RequestBody MyEditDTO editDTO) throws IOException {

        Constants constants = new Constants();
        OtherFunctions otherFunctions = new OtherFunctions();

        String repoOwner = constants.repoOwner;
        String gitToken = constants.gitToken;
        String repoName = constants.repoName;
        String branch = constants.branch; // Or your target branch
        boolean submitFlag = false;

        String movieNameAsEntered = editDTO.getMovieName(); //This is the movie name without spaces
        String movieReview = editDTO.getMovieReview();
        String origEditedName = editDTO.getOrigMovName();

        String movieNameWithSpaces = movieNameAsEntered.substring(0, movieNameAsEntered.length());
        String movieNameWithoutSpaces = movieNameWithSpaces.replaceAll("\\s", ""); //movieName is the inputted name without spaces

        String origEditedNameWithSpaces = origEditedName.substring(0, origEditedName.length());
        String origEditedNameWithoutSpaces = origEditedNameWithSpaces.replaceAll("\\s", ""); //movieName is the inputted name without spaces

        Timer timerCreate = new Timer(); // Create a Timer object
        TimerTask createTask = new TimerTask() {

            @Override
            public void run() {

                try {
                    otherFunctions.writeNewPagesFile(movieNameWithSpaces, movieNameWithoutSpaces, movieReview);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


            }
        };

        Timer timerEdit = new Timer(); // Create a Timer object
        TimerTask editHomeTask = new TimerTask() {

            @Override
            public void run() {

                String filePath = "src/pages/Home.js";

                String origNameWithoutSpaces = null;
                String origNameWithSpaces = null;
                List<String> origNameList = new ArrayList<>();
                String newHomeContent = null;
                GitHub githubHome = null;
                GHRepository repoHome = null;
                GHContent contentHome = null;

                try {

                    origNameList = otherFunctions.getOrigName();
                    origNameWithoutSpaces = origNameList.get(0);
                    origNameWithSpaces = origNameList.get(1);
                    newHomeContent = otherFunctions.editRoutesAppFile(movieNameWithoutSpaces, origEditedNameWithoutSpaces);
                    newHomeContent = otherFunctions.editLinksAppFile(movieNameWithSpaces, movieNameWithoutSpaces, origEditedNameWithSpaces, origEditedNameWithoutSpaces, origNameWithSpaces, origNameWithoutSpaces, newHomeContent);
                    newHomeContent = otherFunctions.addImportLine(movieNameWithoutSpaces, origEditedNameWithoutSpaces, newHomeContent, submitFlag);

                    githubHome = new GitHubBuilder().withOAuthToken(gitToken).build();
                    repoHome = githubHome.getUser(repoOwner).getRepository(repoName);
                    contentHome = repoHome.getFileContent(filePath, branch);
                    repoHome.createContent()
                            .path(filePath)
                            .content(newHomeContent.getBytes("UTF-8"))
                            .message("Updated file via Java API")
                            .sha(contentHome.getSha()) // Important for optimistic locking
                            .branch(branch)
                            .commit();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


            }
        };

        Timer timerDelete = new Timer(); // Create a Timer object
        TimerTask deleteTask = new TimerTask() {
            @Override
            public void run() {
                String deleteFilePath = "src/pages/"+origEditedNameWithoutSpaces+".js";
                try {
                    GitHub githubDelete = new GitHubBuilder().withOAuthToken(gitToken).build();
                    GHRepository repoDelete = githubDelete.getUser(repoOwner).getRepository(repoName);
                    GHContent deletedFile = repoDelete.getFileContent(deleteFilePath, branch);
                    deletedFile.delete("Deleted file via Java API");
                } catch (IOException e) {
                    System.err.println("An error occurred: " + e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        };

        timerDelete.schedule(deleteTask,constants.timeDelay);
        timerCreate.schedule(createTask, constants.timeDelay);

        if (!origEditedNameWithoutSpaces.equals(movieNameWithoutSpaces)){
            timerEdit.schedule(editHomeTask, constants.timeDelay);
        }

        return "You have edited your review. Please wait a few minutes for the website to refresh.";

    }
}
