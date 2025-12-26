package com.example.demo;
import org.kohsuke.github.*;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Sandbox {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello World");
        /*Map<String, String> filesContent = new HashMap();
        filesContent.put("src/pages/England.txt", "London");
        filesContent.put("src/pages/USA.txt", "Washington DC");
        GitHub github = (new GitHubBuilder()).withOAuthToken(system.getEnv(HEDGEHOG)).build();
        GHRepository repo = github.getUser("imtryingmybest45").getRepository("testFrontEndCode");
        GHBranch branch = repo.getBranch("main");
        String baseCommitSha = branch.getSHA1();
        GHTreeBuilder treeBuilder = repo.createTree().baseTree(baseCommitSha);

        for(Map.Entry<String, String> entry : filesContent.entrySet()) {
            String pathToFile = (String)entry.getKey();
            String contentOfFile = (String)entry.getValue();
            treeBuilder.textEntry(pathToFile, contentOfFile, false);
        }

        GHTree newTree = treeBuilder.create();
        String newTreeSha = newTree.getSha();
        GHCommit newCommit = repo.createCommit().message("multifile commit").tree(newTreeSha).parent(baseCommitSha).create();
        GHRef ref = repo.getRef("refs/heads/main");
        boolean forceUpdate = false;
        ref.updateTo(newCommit.getSHA1(), forceUpdate);
        PrintStream var10000 = System.out;
        int var10001 = filesContent.size();
        var10000.println("Successfully committed " + var10001 + " files in a single commit: " + String.valueOf(newCommit.getHtmlUrl()));*/
    }

}
