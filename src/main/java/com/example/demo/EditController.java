package com.example.demo;

import org.kohsuke.github.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
public class EditController {
    @CrossOrigin(origins = {"http://localhost:3000",
            "https://green-smoke-0fa35931e.6.azurestaticapps.net/",
            "https://www.aprilshorrorcorner.com",
            "https://aprilshorrorcorner.com",
            "https://zealous-desert-09313150f.6.azurestaticapps.net/"})

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
        movieNameWithoutSpaces = movieNameWithoutSpaces.replaceAll("[^a-zA-Z0-9]", "");

        String origEditedNameWithSpaces = origEditedName.substring(0, origEditedName.length());
        String origEditedNameWithoutSpaces = origEditedNameWithSpaces.replaceAll("\\s", ""); //movieName is the inputted name without spaces
        origEditedNameWithoutSpaces = origEditedNameWithoutSpaces.replaceAll("[^a-zA-Z0-9]", "");

        String newPagesFileContent = otherFunctions.writeNewPagesFile(movieNameWithSpaces, movieNameWithoutSpaces, movieReview);

        Map<String, String> filesContent = new HashMap<>();
        // Adding items
        filesContent.put("src/pages/"+movieNameWithoutSpaces+".js", newPagesFileContent);

        if (!origEditedNameWithSpaces.equals(movieNameWithSpaces)){
            List<String> origNameList = otherFunctions.getOrigName();
            String origNameWithoutSpaces = origNameList.get(0);
            String origNameWithSpaces = origNameList.get(1);
            String newHomeContent = otherFunctions.editRoutesAppFile(movieNameWithSpaces, movieNameWithoutSpaces, origEditedNameWithSpaces, origEditedNameWithoutSpaces);
            newHomeContent = otherFunctions.editLinksAppFile(movieNameWithSpaces, movieNameWithoutSpaces, origEditedNameWithSpaces, origEditedNameWithoutSpaces, origNameWithSpaces, origNameWithoutSpaces, newHomeContent);
            newHomeContent = otherFunctions.addImportLine(movieNameWithoutSpaces, origEditedNameWithoutSpaces, newHomeContent, submitFlag);

            filesContent.put("src/pages/Home.js", newHomeContent);
        }

        otherFunctions.commitEditedFiles(filesContent, gitToken, repoOwner, repoName, branch, origEditedNameWithoutSpaces, movieNameWithoutSpaces);

        return "You have edited your review. Please wait a few minutes for the website to refresh.";

    }
}
