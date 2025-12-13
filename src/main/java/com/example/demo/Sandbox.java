package com.example.demo;

import org.kohsuke.github.*;
import org.springframework.web.servlet.view.BeanNameViewResolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sandbox {

    public static void main(String[] args) throws Exception {
        String filePath = "C:\\Windows\\System32\\testFrontEndCode\\src\\pages\\Home.js";
        OtherFunctions otherFunctions = new OtherFunctions();
        String origFileCont = otherFunctions.readJsFile(filePath);
        int targetLine = otherFunctions.findSubstringLines(origFileCont, "</Routes>");
        String[] linesArray = origFileCont.split("\r?\n");
        List<String> origFileContList = new ArrayList<>(Arrays.asList(linesArray));
        int lineline = targetLine - 1;
        String desLine = origFileContList.get(lineline).toString();
        int firstIndex = desLine.indexOf("\"");
        int secondIndex = desLine.indexOf("\"", firstIndex + 1);
        String origName = desLine.substring(firstIndex + 2, secondIndex);

        // added 12/13/2025
        int targetLine2 = otherFunctions.findSubstringLines(origFileCont, "return")-2;
        String desLine2 = origFileContList.get(targetLine2).toString();
        int firstIndex2 = otherFunctions.findNthOccurrence(desLine2, "'",1);
        int secondIndex2 = otherFunctions.findNthOccurrence(desLine2, "'",2);
        String origNameAsTyped = desLine2.substring(firstIndex2 + 1, secondIndex2);

        List<String> nameList = new ArrayList<>();
        nameList.add(origName);
        nameList.add(origNameAsTyped);

        String origNameWithSpaces = nameList.get(1);
        String nextString = "The Autopsy of Jane Doe";

        String name = "TheAutopsyOfJaneDoe";

        int targetLine23 = otherFunctions.findSubstringLines(origFileCont, "</Routes>");
        String[] linesArray2 = origFileCont.split("\r?\n");
        List<String> origFileContList2 = new ArrayList<>(Arrays.asList(linesArray2));
        int lineline2 = targetLine - 1;
        String desLine23 = origFileContList2.get(lineline2).toString();
        int firstIndex23 = desLine23.indexOf("\"");
        int secondIndex23 = desLine23.indexOf("\"", firstIndex23 + 1);
        String origName2 = desLine23.substring(firstIndex23 + 2, secondIndex23);
        String newContentLine = desLine.replace(origName2, name);
        origFileContList.add(targetLine23, newContentLine);
        String newContentRev1 = String.join("\n", origFileContList);

        int targetLine34 = otherFunctions.findSubstringLines(newContentRev1, "return");
        String[] newContRev1StrList = newContentRev1.split("\r?\n");
        List<String> newContRev1StrArrList = new ArrayList<>(Arrays.asList(newContRev1StrList));
        int prevTargetLine = targetLine34 - 2;
        String desString = newContRev1StrArrList.get(prevTargetLine).toString();
        String newLinkNumber = Integer.toString(otherFunctions.getIDNumber(desString));
        String prevLinkNumber = Integer.toString(otherFunctions.getIDNumber(desString) - 1);
        desString = desString.replaceFirst(prevLinkNumber, newLinkNumber);
        //String origNameWithSpaces = addSpacesToString(origName);
        //String newNameWithSpaces = addSpacesToString(name);
        //desString = desString.replaceFirst(origNameWithSpaces, newNameWithSpaces);
        desString = desString.replaceFirst(origNameWithSpaces, nextString);
        desString = desString.replaceFirst(origName, name);

        newContRev1StrArrList.add(prevTargetLine + 1, desString);
        String newContentRev2 = String.join("\n", newContRev1StrArrList);

        System.out.println(newContentRev2);


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
