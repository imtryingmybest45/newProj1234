package com.example.demo;

import org.kohsuke.github.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

import static com.example.demo.OtherFunctions.removeCharsAtIndices;

@RestController
public class EditController {
    @CrossOrigin(origins = {"http://localhost:3000",
            "https://green-smoke-0fa35931e.6.azurestaticapps.net/",
            "https://www.aprilshorrorcorner.com",
            "https://aprilshorrorcorner.com",
            "https://zealous-desert-09313150f.6.azurestaticapps.net/",
            "https://help.aprilshorrorcorner.com"})

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
        String movieTier = editDTO.getMovieTier();
        String movieYear = editDTO.getMovieYear();

        String movieNameWithSpaces = movieNameAsEntered.substring(0, movieNameAsEntered.length());
        String movieNameWithoutSpaces = movieNameWithSpaces.replaceAll("\\s", ""); //movieName is the inputted name without spaces
        movieNameWithoutSpaces = movieNameWithoutSpaces.replaceAll("[^a-zA-Z0-9]", "");

        char firstChar = movieNameWithoutSpaces.charAt(0);
        if (Character.isDigit(firstChar)){
            movieNameWithoutSpaces = "X"+movieNameWithoutSpaces;
        }
        
        String movieQuery = movieNameWithSpaces;

        if (movieQuery.contains("(")){
            int n = 0;
            int index = movieQuery.indexOf("(");
            for (int i = 1; i < 5; i++) {
                char V =  movieQuery.charAt(index+i);
                if (Character.isDigit(V)){
                    n = n+1;
                }
            }
            if (n==4){
                Integer[] indices = {index, index+1, index+2, index+3, index+4, index+5};
                movieQuery = removeCharsAtIndices(movieQuery,indices);
            }
        }

        String origEditedNameWithSpaces = origEditedName.substring(0, origEditedName.length());
        String origEditedNameWithoutSpaces = origEditedNameWithSpaces.replaceAll("\\s", ""); //movieName is the inputted name without spaces
        origEditedNameWithoutSpaces = origEditedNameWithoutSpaces.replaceAll("[^a-zA-Z0-9]", "");

        String newPagesFileContent = otherFunctions.writeNewPagesFile(movieNameWithSpaces, movieNameWithoutSpaces, movieReview, movieTier, movieYear, movieQuery);

        Map<String, String> filesContent = new HashMap<>();
        // Adding items
        filesContent.put("src/pages/" + movieNameWithoutSpaces + ".js", newPagesFileContent);

        String newHomeContent;
        if (!origEditedNameWithSpaces.equals(movieNameWithSpaces)) {
            List<String> origNameList = otherFunctions.getOrigName();
            String origNameWithoutSpaces = origNameList.get(0);
            String origNameWithSpaces = origNameList.get(1);
            newHomeContent = otherFunctions.editRoutesAppFile(movieNameWithSpaces, movieNameWithoutSpaces, origEditedNameWithSpaces, origEditedNameWithoutSpaces);
            newHomeContent = otherFunctions.editLinksAppFile(movieNameWithSpaces, movieNameWithoutSpaces, origEditedNameWithSpaces, origEditedNameWithoutSpaces, origNameWithSpaces, origNameWithoutSpaces, movieTier, newHomeContent,movieReview, movieYear, movieQuery);
            newHomeContent = otherFunctions.addImportLine(movieNameWithoutSpaces, origEditedNameWithoutSpaces, newHomeContent, submitFlag);

            filesContent.put("src/pages/Home.js", newHomeContent);
        } else {
            newHomeContent = otherFunctions.editTier(movieNameWithSpaces, movieNameWithoutSpaces, origEditedNameWithSpaces, origEditedNameWithoutSpaces, movieTier, movieReview, movieYear, movieQuery);
            filesContent.put("src/pages/Home.js", newHomeContent);
        }

        otherFunctions.commitEditedFiles(filesContent, gitToken, repoOwner, repoName, branch, origEditedNameWithoutSpaces, movieNameWithoutSpaces);

        return "You have edited your review. Please wait a few minutes for the website to refresh.";

    }
}
