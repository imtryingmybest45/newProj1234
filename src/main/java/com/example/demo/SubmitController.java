package com.example.demo;
import org.kohsuke.github.*;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.*;

import static com.example.demo.OtherFunctions.removeCharsAtIndices;

@RestController
public class SubmitController {
    @CrossOrigin(origins = {"http://localhost:3000",
            "https://green-smoke-0fa35931e.6.azurestaticapps.net/",
            "https://www.aprilshorrorcorner.com",
            "https://aprilshorrorcorner.com",
            "https://zealous-desert-09313150f.6.azurestaticapps.net/",
            "https://help.aprilshorrorcorner.com"})

    @PostMapping("/submitEndpoint")

    public String getData(@RequestBody MyRequestDTO requestDTO) throws IOException {

        Constants constants = new Constants();
        OtherFunctions otherFunctions = new OtherFunctions();

        String repoName = constants.repoName;
        String gitToken = constants.gitToken;
        String repoOwner = constants.repoOwner;
        String branch = constants.branch; // Or your target branch

        boolean submitFlag = true;

        String movieNameAsEntered = requestDTO.getMovieName(); //This is the movie name without spaces
        String movieReview = requestDTO.getMovieReview();
        String movieTier = requestDTO.getMovieTier();
        String movieYear = requestDTO.getMovieYear();

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
                Integer[] indices = {index-1, index, index+1, index+2, index+3, index+4, index+5};
                movieQuery = removeCharsAtIndices(movieQuery,indices);
            }
        }

        String origEditedNameWithSpaces = movieNameWithSpaces;
        String origEditedNameWithoutSpaces = movieNameWithoutSpaces;

        String newPagesFileContent = otherFunctions.writeNewPagesFile(movieNameWithSpaces, movieNameWithoutSpaces, movieReview, movieTier, movieYear, movieQuery);

        List<String> origNameList = otherFunctions.getOrigName();
        String origNameWithoutSpaces = origNameList.get(0);
        String origNameWithSpaces = origNameList.get(1);
        String newHomeContent = otherFunctions.editRoutesAppFile(movieNameWithSpaces, movieNameWithoutSpaces, origEditedNameWithSpaces, origEditedNameWithoutSpaces);
        newHomeContent = otherFunctions.editLinksAppFile(movieNameWithSpaces, movieNameWithoutSpaces, origEditedNameWithSpaces, origEditedNameWithoutSpaces, origNameWithSpaces, origNameWithoutSpaces, movieTier, newHomeContent, movieReview, movieYear, movieQuery);
        newHomeContent = otherFunctions.addImportLine(movieNameWithoutSpaces, origEditedNameWithoutSpaces, newHomeContent, submitFlag);

        Map<String, String> filesContent = new HashMap<>();

        // Adding items
        filesContent.put("src/pages/"+movieNameWithoutSpaces+".js", newPagesFileContent);
        filesContent.put("src/pages/Home.js", newHomeContent);

        otherFunctions.submitMultipleFiles(filesContent, gitToken, repoOwner, repoName, branch);

        return "You have submitted your review. Please wait a few minutes for the website to refresh.";

    }
}

