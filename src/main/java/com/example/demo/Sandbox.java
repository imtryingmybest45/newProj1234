package com.example.demo;

import org.kohsuke.github.*;
import org.springframework.web.servlet.view.BeanNameViewResolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Sandbox {

    public static void main(String[] args) throws Exception {
        String movieName = "The Autopsy of Jane Doe";
        String filePath = "C:\\Windows\\System32\\testFrontEndCode\\src\\pages\\Home.js";
        OtherFunctions otherFunctions = new OtherFunctions();
        String origFileCont = otherFunctions.readJsFile(filePath);
        String[] words = origFileCont.split("\\R");
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(words));
        String movNameWithoutSpaces = movieName.replaceAll("\\s+", "");

        for(int i=0;i<arrayList.size();i++) {
            if (words[i].contains("\"/" +movNameWithoutSpaces+'\"')) {
                arrayList.set(i,"Line to Remove");
            }
            if (words[i].contains("'./" +movNameWithoutSpaces+"'")) {
                arrayList.set(i,"Line to Remove");
            }
            if (words[i].contains("to: '/" +movNameWithoutSpaces+"'")) {
                arrayList.set(i,"Line to Remove");
            }
        }
        arrayList.removeAll(Collections.singleton("Line to Remove"));
        String joinedString = String.join("\n", arrayList);
        System.out.println(joinedString);
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
