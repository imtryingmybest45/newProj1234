package com.example.demo;

import org.kohsuke.github.*;
import org.springframework.web.servlet.view.BeanNameViewResolver;

import java.io.IOException;
import java.util.*;

public class Sandbox {

    public static void main(String[] args) throws Exception {
        String content = "New Movie Name";
        String movieReview = "moviewReview";

        String nextString = content.substring(0, content.length());
        String newString = nextString.replaceAll("\\s", "");

        OtherFunctions otherFunctions = new OtherFunctions();
        String name = newString;
        String repoName = "frontEndAppCode";
        String gitToken = System.getenv("HEDGEHOG");
        System.out.println("Git Token: " + gitToken);

        //otherFunctions.writeNewPagesFile(nextString, name,repoName,gitToken,movieReview);
        System.out.println("checkpoint0");
        Timer timer = new Timer(); // Create a Timer object
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                String origName = null;
                String origNameWithSpaces = null;
                List<String> fruits = new ArrayList<>();
                System.out.println("chekpoint1");
                try {
                    fruits = otherFunctions.getOrigName(repoName,gitToken);
                    origName = fruits.get(0);
                    origNameWithSpaces = fruits.get(1);
                    System.out.println("chekpoint2");
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
                    newContent2 = otherFunctions.editLinksAppFile(name, origName, origNameWithSpaces, newContent, nextString);
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


            }
        };
        timer.schedule(task,6000);
    }
}