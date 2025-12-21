package com.example.demo;
import org.kohsuke.github.*;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@RestController
public class SubmitController {
    @CrossOrigin(origins = {"http://localhost:3000",
            "https://delightful-mushroom-0b98f760f.3.azurestaticapps.net/",
            "https://www.aprilshorrorcorner.com",
            "https://aprilshorrorcorner.com"})

    @PostMapping("/submitEndpoint")

    public String getData(@RequestBody MyRequestDTO requestDTO) throws IOException {

        Constants constants = new Constants();
        OtherFunctions otherFunctions = new OtherFunctions();

        String repoName = constants.repoName;
        String gitToken = constants.gitToken;
        boolean submitFlag = true;

        String movieNameAsEntered = requestDTO.getMovieName(); //This is the movie name without spaces
        String movieReview = requestDTO.getMovieReview();

        String movieNameWithSpaces = movieNameAsEntered.substring(0, movieNameAsEntered.length());
        String movieNameWithoutSpaces = movieNameWithSpaces.replaceAll("\\s", ""); //movieName is the inputted name without spaces

        String origEditedNameWithSpaces = movieNameWithSpaces;
        String origEditedNameWithoutSpaces = movieNameWithoutSpaces;

        otherFunctions.writeNewPagesFile(movieNameWithSpaces, movieNameWithoutSpaces, movieReview);

        Timer timer = new Timer(); // Create a Timer object
        TimerTask task = new TimerTask() {

            @Override
            public void run() {

                String repoOwner = constants.repoOwner;
                String filePath = "src/pages/Home.js";
                String branch = constants.branch; // Or your target branch

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
        timer.schedule(task,constants.timeDelay);
        return "You have submitted your review. Please wait a few minutes for the website to refresh.";

    }
}

