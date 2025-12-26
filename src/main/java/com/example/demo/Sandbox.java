package com.example.demo;
import org.kohsuke.github.*;

import java.io.IOException;
import java.util.Collections;

public class Sandbox {
   /* public static void commitMultipleFiles(GHRepository repo, String branchName, String commitMessage, java.util.Map<String, String> filesContent) throws IOException {
        // 1. Get the SHA of the latest commit on the target branch
        GHBranch branch = repo.getBranch(branchName);
        String latestCommitSHA = branch.getSHA1();

        // 2. Get the tree information for that commit to use as the base
        GHTree baseTree = repo.getGitCommit(latestCommitSHA).getTree();

        // 3. Create a new tree builder object
        GHTreeBuilder treeBuilder = repo.createTree().baseTree(baseTree.getSHA1());

        // 4. Add all file entries to the tree builder
        for (java.util.Map.Entry<String, String> entry : filesContent.entrySet()) {
            String pathToFile = entry.getKey();
            String contentOfFile = entry.getValue();
            // textEntry automatically creates the necessary blob object for the content
            treeBuilder.textEntry(pathToFile, contentOfFile);
        }

        // 5. Create the new tree
        GHTree newTree = treeBuilder.create();

        // 6. Create the commit, referencing the new tree and the parent commit
        GHCommit newCommit = repo.createCommit()
                .message(commitMessage)
                .tree(newTree.getSha())
                .parents(Collections.singletonList(latestCommitSHA))
                .create();

        // 7. Update the branch reference to point to the new commit
        // This is the final step that pushes the changes to the branch
        repo.updateRef("refs/heads/" + branchName, newCommit.getSHA1());
    }*/
}