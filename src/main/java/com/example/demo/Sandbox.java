package com.example.demo;

import org.kohsuke.github.*;
import org.springframework.web.servlet.view.BeanNameViewResolver;

public class Sandbox {

    public static void main(String[] args) throws Exception {
        String githubToken = System.getenv("MY_AWESOME_PAT");
        //System.out.println("apiKey: " + apiKey);
        String owner = "imtryingmybest45";
        String repoName = "backEndAppCode";
        String filePath = "demo/src/main/java/com/example/demo/DemoApplication.java";
        String branch = "main"; // Or your target branch

        GitHub github = new GitHubBuilder().withOAuthToken(githubToken).build();
        GHRepository repo = github.getUser(owner).getRepository(repoName);

        /*String filePath2 = "demo/src/main/java/com/example/demo/hellYeah.js";
        String fileContent = "This is the content of the new file.";
        String commitMessage = "Add hellYeah";

        repo.createContent(fileContent, commitMessage, filePath2);
        System.out.println("File '" + filePath + "' added successfully.");*/

       // 1. Fetch file content
        GHContent content = repo.getFileContent(filePath, branch);
        String currentContent = new String(content.read().readAllBytes(), "UTF-8");

        // 2. Modify content (example: append a line)
        String newContent = currentContent + "\n// Added by Java application";

        // 3. Commit and push changes
        repo.createContent()
                .path(filePath)
                .content(newContent.getBytes("UTF-8"))
                .message("Updated file via Java API")
                .sha(content.getSha()) // Important for optimistic locking
                .branch(branch)
                .commit();

        System.out.println("File updated successfully!");
    }
}




/*package com.example.demo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Sandbox {

    private static final String GITHUB_API_BASE_URL = "https://github.com/";
    private static final String GITHUB_TOKEN = "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"; // Replace with your token

    public static void updateFileInRepo(String owner, String repo, String filePath, String newContent, String commitMessage) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();

        // 1. Get current file content and SHA
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(GITHUB_API_BASE_URL + owner + "/" + repo + "/blob/main/" + filePath))
                .header("Authorization", "token " + GITHUB_TOKEN)
                .build();
        System.out.println(getRequest.uri());
        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        String jsonBody = getResponse.body();
        Document doc = Jsoup.parse(jsonBody);
        Elements scriptElement = doc.select("script[type='application/json']");
        String newStr = scriptElement.last().toString().replaceAll("script", "pre");
        Document doc2 = Jsoup.parse(newStr);
        String jsonString = doc2.text();
        //System.out.println(jsonString);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonString);

        //System.out.println(rootNode.fieldNames());

        if (rootNode.get("payload").get("blob").has("rawLines")) {
            System.out.println("Name: " + rootNode.get("payload").get("blob").get("rawLines"));
            //System.out.println("hello");
        }

        String prettyJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        System.out.println("Pretty-printed JSON structure:\n" + prettyJson);

        /*String currentContentBase64 = extractContentFromGithubResponse(jsonBody); // Implement this extraction
        String currentSha = extractShaFromGithubResponse(jsonBody); // Implement this extraction

        // 2. Encode new content
        String newContentBase64 = Base64.getEncoder().encodeToString(newContent.getBytes());

        // 3. Create update request
        String requestBody = String.format(
                "{\"message\": \"%s\", \"content\": \"%s\", \"sha\": \"%s\"}",
                commitMessage, newContentBase64, currentSha
        );

        HttpRequest putRequest = HttpRequest.newBuilder()
                .uri(URI.create(GITHUB_API_BASE_URL + "/repos/" + owner + "/" + repo + "/contents/" + filePath))
                .header("Authorization", "token " + GITHUB_TOKEN)
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> putResponse = client.send(putRequest, HttpResponse.BodyHandlers.ofString());
        //System.out.println("Update response: " + putResponse.body());
    }

    // Placeholder methods for extracting content and SHA from GitHub API response
    private static String extractContentFromGithubResponse(String json) {
        // Implement JSON parsing to get the 'content' field
        return "base64encodedoldcontent"; // Replace with actual extraction
    }

    private static String extractShaFromGithubResponse(String json) {
        // Implement JSON parsing to get the 'sha' field
        return "oldsha"; // Replace with actual extraction
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        // Example usage:
        updateFileInRepo("imtryingmybest45", "backEndAppCode", "demo/src/main/java/com/example/demo/DemoApplication.java", "New content of the file.", "Updated file via Java API");
    }
}*/
