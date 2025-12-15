package com.example.demo;

import org.kohsuke.github.*;
import org.springframework.web.servlet.view.BeanNameViewResolver;

import java.io.IOException;
import java.util.*;

public class Sandbox {

    public static void main(String[] args) throws Exception {
        String content = "Insidious 2";
        String movieReview = "This is a movie review";

        String nextString = content.substring(0, content.length());
        String newString = nextString.replaceAll("\\s", "");

        OtherFunctions otherFunctions = new OtherFunctions();
        String name = newString;
        String repoName = "frontEndAppCode";
        String gitToken = System.getenv("HEDGEHOG");
        //System.out.println("Git Token: " + gitToken);

        //otherFunctions.writeNewPagesFile(nextString, name,repoName,gitToken,movieReview);
        List<String> fruits = otherFunctions.getOrigName(repoName,gitToken);
        String origName = fruits.get(0);
        String origNameWithSpaces = fruits.get(1);
        String newContent = otherFunctions.editRoutesAppFile(name,repoName,gitToken);
        String newContent2 = otherFunctions.editLinksAppFile(name, origName, origNameWithSpaces, newContent, nextString);
        String newContent3 = otherFunctions.addImportLine(name, newContent2);

        System.out.println(newContent3);

        //String githubToken = gitToken; // Replace with your token
        String owner = "imtryingmybest45";
        String filePath = "src/pages/Home.js";
        String branch = "main"; // Or your target branch

        GitHub github2 = new GitHubBuilder().withOAuthToken(gitToken).build();
        GHRepository repo2 = github2.getUser(owner).getRepository(repoName);

        GHContent stuff = repo2.getFileContent(filePath, branch);
        String currentContent = new String(stuff.read().readAllBytes(), "UTF-8");

        /*repo2.createContent()
            .path(filePath)
            .content(newContent3.getBytes("UTF-8"))
            .message("Updated file via Java API")
            .sha(stuff.getSha()) // Important for optimistic locking
            .branch(branch)
            .commit();*/
    }
}