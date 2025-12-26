package com.example.demo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.kohsuke.github.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;

public class OtherFunctions {

    public static int getIDNumber(String s) {
        ArrayList<Integer> list = new ArrayList<>();
        int currNumDigit = 0;
        int prevNumDigit = 0;
        int currChar = 0;
        while (currNumDigit - prevNumDigit >= 0) {
            prevNumDigit = currNumDigit;
            if (Character.isDigit(s.charAt(currChar))) {
                currNumDigit = 1;
                list.add(currChar);
            } else {
                currNumDigit = 0;
            }
            currChar++;
        }
        String numAsString = s.substring(list.get(0), list.get(list.size() - 1) + 1);
        int number = Integer.parseInt(numAsString);
        int idNumber = number + 1;
        return idNumber;
    }

    public static String modifyJsContent(String originalContent, String movieNameWithoutSpaces) {
        return originalContent.replace("NewFunc", movieNameWithoutSpaces);
    }

    public static int findSubstringLines(String mainString, String substring) {
        long lineCount = mainString.lines().count();
        int theLineNum = 0;
        for (int i = 0; i < lineCount; i++) {
            Optional<String> desiredLine = mainString.lines().skip(i).findFirst();
            if (desiredLine.toString().contains(substring)) {
                theLineNum = i;
            }
        }
        return theLineNum;
    }

    public static String writeNewPagesFile(String movieNameWithSpaces, String movieNameWithoutSpaces, String movieReview) throws IOException {

        Constants constants = new Constants();

        String gitToken = System.getenv("HEDGEHOG");
        String repoOwner = constants.repoOwner;
        String repoName = constants.repoName;
        String branch = constants.branch; // Or your target branch
        String funcTemplateFilePath = "src/NewFunc.js";

        GitHub github = new GitHubBuilder().withOAuthToken(gitToken).build();
        GHRepository repo = github.getUser(repoOwner).getRepository(repoName);

        GHContent funcTemplateFileContent = repo.getFileContent(funcTemplateFilePath, branch);
        String funcFileContent = new String(funcTemplateFileContent.read().readAllBytes(), "UTF-8");

        String fileContent = modifyJsContent(funcFileContent, movieNameWithoutSpaces);
        String poster = getMoviePoster(movieNameWithSpaces.replaceAll(" ","+"));

        movieReview = movieReview.replace("\n", "\\n");
        movieReview = movieReview.replace("\"","\\\"");

        fileContent = fileContent.replace("Insert movie name here",movieNameWithSpaces);
        fileContent = fileContent.replace("Insert movie review here",movieReview); //originally said modified string
        fileContent = fileContent.replace("Insert Movie Poster Here",poster);

        return fileContent;
    }

    public static int findNthOccurrence(String text, String subString, int n) {
        if (text == null || subString == null || n <= 0) {
            return -1; // Invalid input
        }

        int index = -1;
        int count = 0;

        while (count < n) {
            index = text.indexOf(subString, index + 1); // Start search from after the last found index
            if (index == -1) {
                return -1; // Substring not found 'n' times
            }
            count++;
        }
        return index;
    }

    public static List<String> getOrigName() throws IOException {

        Constants constants = new Constants();

        String gitToken = System.getenv("HEDGEHOG");
        String repoOwner = constants.repoOwner;
        String repoName = constants.repoName;
        String branch = constants.branch; // Or your target branch
        String homeFilePath = "src/pages/Home.js";

        GitHub github = new GitHubBuilder().withOAuthToken(gitToken).build();
        GHRepository repo = github.getUser(repoOwner).getRepository(repoName);

        GHContent stuff = repo.getFileContent(homeFilePath, branch);
        String homeOrigFileCont = new String(stuff.read().readAllBytes(), "UTF-8");

        int targetLine = findSubstringLines(homeOrigFileCont, "</Routes>");
        String[] linesArray = homeOrigFileCont.split("\r?\n");
        List<String> homeOrigFileContList = new ArrayList<>(Arrays.asList(linesArray));
        String desLine = homeOrigFileContList.get(targetLine - 1).toString();
        int firstIndex = desLine.indexOf("\"");
        int secondIndex = desLine.indexOf("\"", firstIndex + 1);
        String origName = desLine.substring(firstIndex + 2, secondIndex);

        int targetLine2 = findSubstringLines(homeOrigFileCont, "//const stvar = \"hello\";")-7;
        String desLine2 = homeOrigFileContList.get(targetLine2).toString();
        int firstIndex2 = findNthOccurrence(desLine2, "'",1);
        int secondIndex2 = findNthOccurrence(desLine2, "'",2);
        String origNameAsTyped = desLine2.substring(firstIndex2 + 1, secondIndex2);

        List<String> origNameList = new ArrayList<>();
        origNameList.add(origName);
        origNameList.add(origNameAsTyped);

        return origNameList;
    }
    public static String editRoutesAppFile(String movieNameWithoutSpaces, String origEditedNameWithoutSpaces) throws IOException {

        Constants constants = new Constants();

        String gitToken = System.getenv("HEDGEHOG");
        String repoOwner = constants.repoOwner;
        String repoName = constants.repoName;
        String branch = constants.branch; // Or your target branch
        String homeFilePath = "src/pages/Home.js";

        GitHub github = new GitHubBuilder().withOAuthToken(gitToken).build();
        GHRepository repo = github.getUser(repoOwner).getRepository(repoName);

        GHContent stuff = repo.getFileContent(homeFilePath, branch);
        String origHomeFileCont = new String(stuff.read().readAllBytes(), "UTF-8");

        String[] linesArray = origHomeFileCont.split("\r?\n");
        List<String> origHomeFileContList = new ArrayList<>(Arrays.asList(linesArray));

        if(origEditedNameWithoutSpaces.equals(movieNameWithoutSpaces)) {

            int targetLine = findSubstringLines(origHomeFileCont, "</Routes>");
            String desLine = origHomeFileContList.get(targetLine - 1).toString();
            int firstIndex = desLine.indexOf("\"");
            int secondIndex = desLine.indexOf("\"", firstIndex + 1);
            String origName = desLine.substring(firstIndex + 2, secondIndex);
            String newContentLine = desLine.replace(origName, movieNameWithoutSpaces);
            origHomeFileContList.add(targetLine, newContentLine);
        }
        else{
            int targetLine = findSubstringLines(origHomeFileCont, "<Route path=\"/"+origEditedNameWithoutSpaces+"\"");
            String desLine = origHomeFileContList.get(targetLine).toString();
            desLine = desLine.replace(origEditedNameWithoutSpaces,movieNameWithoutSpaces);
            origHomeFileContList.set(targetLine, desLine);
        }

        String newHomeContent = String.join("\n", origHomeFileContList);
        return newHomeContent;
    }

    public static String editLinksAppFile(String movieNameWithSpaces, String movieNameWithoutSpaces, String origEditedNameWithSpaces, String origEditedNameWithoutSpaces, String origNameWithSpaces, String origNameWithoutSpaces, String newHomeContent) throws IOException {

        String[] newHomeContentList = newHomeContent.split("\r?\n");
        List<String> newHomeContentArrList = new ArrayList<>(Arrays.asList(newHomeContentList));

        if(origEditedNameWithoutSpaces.equals(movieNameWithoutSpaces)) {

            int targetLine = findSubstringLines(newHomeContent, "//const stvar = \"hello\";");
            //String desLine = newHomeContentArrList.get(targetLine-3).toString();
            String desLine = newHomeContentArrList.get(targetLine-7).toString();
            String newLinkNumber = Integer.toString(getIDNumber(desLine));
            String prevLinkNumber = Integer.toString(getIDNumber(desLine) - 1);
            desLine = desLine.replaceFirst(prevLinkNumber, newLinkNumber);

            desLine = desLine.replaceFirst("'"+origNameWithSpaces+"'", "'"+movieNameWithSpaces+"'");
            desLine = desLine.replaceFirst("'/"+origNameWithoutSpaces+"'", "'/"+movieNameWithoutSpaces+"'");
            desLine = desLine.replaceFirst("\""+origNameWithSpaces+"\"", "\""+movieNameWithSpaces+"\"");
            newHomeContentArrList.add(targetLine-6, desLine);
            //newHomeContentArrList.add(targetLine-2, desLine);
        }
        else{
            int targetLine = findSubstringLines(newHomeContent, "text: '"+origEditedNameWithSpaces+"'");
            String desLine = newHomeContentArrList.get(targetLine).toString();
            desLine = desLine.replaceFirst("'"+origEditedNameWithSpaces+"'", "'"+movieNameWithSpaces+"'");
            desLine = desLine.replaceFirst("'/"+origEditedNameWithoutSpaces+"'", "'/"+movieNameWithoutSpaces+"'");
            desLine = desLine.replaceFirst("\""+origEditedNameWithSpaces+"\"", "\""+movieNameWithSpaces+"\"");
            newHomeContentArrList.set(targetLine, desLine);
        }

        String newContentRev2 = String.join("\n", newHomeContentArrList);
        return newContentRev2;
    }
    public static String addImportLine(String movieNameWithoutSpaces, String origEditedNameWithoutSpaces, String newHomeContent, boolean submitFlag) throws IOException {
        String desLine = "import " + movieNameWithoutSpaces + " from " + "'./" + movieNameWithoutSpaces + "';";
        String[] newHomeContentList = newHomeContent.split("\r?\n");
        List<String> newHomeContentArrList= new ArrayList<>(Arrays.asList(newHomeContentList));
        if(!submitFlag) {
            int targetLine = findSubstringLines(newHomeContent, "import " + origEditedNameWithoutSpaces + " from " + "'./" + origEditedNameWithoutSpaces + "';");
            String newDesLine = "import " + movieNameWithoutSpaces + " from " + "'./" + movieNameWithoutSpaces + "';";
            newHomeContentArrList.set(targetLine, newDesLine);
        }
        else{
            newHomeContentArrList.add(0, desLine);
        }
        String newContentRev3 = String.join("\n", newHomeContentArrList);
        return newContentRev3;
    }

    public static String addSpacesToString(String s){
        String modifiedString = s.replaceAll("(?<!^)(?=[A-Z0-9])", " ");
        //String modifiedString = s.replaceAll("(?<!^)(?=[A-Z])", " ");

        String[] words = modifiedString.split("\\s+");
        List<String> wordsList = new ArrayList<>();
        for (String word : words) {
            wordsList.add(word);
        }
        List<String> myStringList = new ArrayList<>();
        myStringList.add("A");
        myStringList.add("Of");
        myStringList.add("The");
        myStringList.add("Is");
        myStringList.add("In");
        myStringList.add("Or");
        myStringList.add("For");
        for (int i=1; i<words.length; i++) {
            String word = words[i];
            if (myStringList.contains(word)) {
                String lowerWord = word.toLowerCase();
                wordsList.set(i, lowerWord);
            }
        }
        String combinedString = String.join(" ", wordsList);
        return combinedString;
    }
    public static String removeFile(String movieName, String origFileCont){

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
        return joinedString;
    }

    public static String replaceSecond(String originalString, String targetSubstring, String replacement) {
        // Find the index of the first occurrence
        int firstIndex = originalString.indexOf(targetSubstring);

        if (firstIndex == -1) {
            // Target substring not found at all, return original
            return originalString;
        }

        // Find the index of the second occurrence, starting the search after the first one
        int secondIndex = originalString.indexOf(targetSubstring, firstIndex + targetSubstring.length());

        if (secondIndex == -1) {
            // Second occurrence not found, return original
            return originalString;
        }

        // Build the new string using substring concatenation
        // 1. Substring from the beginning to the start of the second occurrence
        String partBefore = originalString.substring(0, secondIndex);
        // 2. Substring from the end of the second occurrence to the end of the string
        String partAfter = originalString.substring(secondIndex + targetSubstring.length());

        // Concatenate the parts with the replacement in the middle
        return partBefore + replacement + partAfter;
    }

    public static String getMoviePoster(String movieQuery) throws JsonProcessingException {
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        // 2. Build the HttpRequest object
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://www.omdbapi.com/?t="+movieQuery+"&apikey=98f9696d")) // Replace with your API URL
                .header("Accept", "application/json") // Common header for JSON APIs
                .GET() // Specify the HTTP method (GET, POST, PUT, DELETE, etc.)
                .build();
        String posterResp = null;
        String poster = null;
        try {
            // 3. Send the request synchronously and receive the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // 4. Check the response status code and body
            System.out.println("Status Code: " + response.statusCode());
            if (response.statusCode() == 200) {
                System.out.println("Response Body: " + response.body());

                posterResp = response.body();

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                // Deserialize the JSON string directly into a Person object
                Poster moviePoster = objectMapper.readValue(posterResp, Poster.class);
                String responseString = posterResp.toString();

                if(responseString.equals("{\"Response\":\"False\",\"Error\":\"Movie not found!\"}")){
                    poster = "error";
                }
                else{
                    poster = moviePoster.getPoster();
                }
                // You would typically parse the JSON response here
            } else {
                System.err.println("API request failed with status code: " + response.statusCode());
                poster = "error";
            }

        } catch (Exception e) {
            e.printStackTrace();
            poster = "error";
        }
        return poster;
    }

    public static void submitMultipleFiles(Map<String, String> filesContent, String gitToken, String repoOwner, String repoName, String branchName) throws IOException {

        GitHub github = new GitHubBuilder().withOAuthToken(gitToken).build();
        GHRepository repo = github.getUser(repoOwner).getRepository(repoName);
        // 1. Get the SHA of the latest commit on the target branch
        GHBranch branch = repo.getBranch(branchName);
        String baseCommitSha = branch.getSHA1();

        // 2. Get a tree builder object with the base of the current branch
        GHTreeBuilder treeBuilder = repo.createTree().baseTree(baseCommitSha);

        // 3. Add entries for all files to the tree builder
        // The textEntry method acts as an "upsert" (create new or update existing)
        for (Map.Entry<String, String> entry : filesContent.entrySet()) {
            String pathToFile = entry.getKey();
            String contentOfFile = entry.getValue();
            treeBuilder.textEntry(pathToFile, contentOfFile,false);
        }

        // 4. Create the new tree on GitHub
        GHTree newTree = treeBuilder.create();
        String newTreeSha = newTree.getSha();

        // 5. Create the commit pointing to the new tree
        GHCommit newCommit = repo.createCommit()
                .message("multifile commit")
                .tree(newTreeSha)
                .parent(baseCommitSha)
                .create();
        GHRef ref = repo.getRef("refs/heads/" + branchName);

        boolean forceUpdate = false;
        ref.updateTo(newCommit.getSHA1(), forceUpdate);

        // 6. Update the branch reference to point to the new commit
        //repo.updateRef("refs/heads/" + branchName, newCommit.getSHA1());

        System.out.println("Successfully committed " + filesContent.size() + " files in a single commit: " + newCommit.getHtmlUrl());
    }

    public static void commitEditedFiles(Map<String, String> filesContent, String gitToken, String repoOwner, String repoName, String branchName, String origMovieNameWithoutSpaces, String movieNameWithoutSpaces) throws IOException {

        GitHub github = new GitHubBuilder().withOAuthToken(gitToken).build();
        GHRepository repo = github.getUser(repoOwner).getRepository(repoName);
        // 1. Get the SHA of the latest commit on the target branch
        GHBranch branch = repo.getBranch(branchName);
        String baseCommitSha = branch.getSHA1();

        // 2. Get a tree builder object with the base of the current branch
        GHTreeBuilder treeBuilder = repo.createTree().baseTree(baseCommitSha);

        // 3. Add entries for all files to the tree builder
        // The textEntry method acts as an "upsert" (create new or update existing)
        for (Map.Entry<String, String> entry : filesContent.entrySet()) {
            String pathToFile = entry.getKey();
            String contentOfFile = entry.getValue();
            treeBuilder.textEntry(pathToFile, contentOfFile,false);
        }

        if (!origMovieNameWithoutSpaces.equals(movieNameWithoutSpaces)){
            treeBuilder.delete("src/pages/"+origMovieNameWithoutSpaces+".js");
        }

        // 4. Create the new tree on GitHub
        GHTree newTree = treeBuilder.create();
        String newTreeSha = newTree.getSha();

        // 5. Create the commit pointing to the new tree
        GHCommit newCommit = repo.createCommit()
                .message("multifile commit")
                .tree(newTreeSha)
                .parent(baseCommitSha)
                .create();
        GHRef ref = repo.getRef("refs/heads/" + branchName);

        boolean forceUpdate = false;
        ref.updateTo(newCommit.getSHA1(), forceUpdate);

        // 6. Update the branch reference to point to the new commit
        //repo.updateRef("refs/heads/" + branchName, newCommit.getSHA1());

        System.out.println("Successfully committed " + filesContent.size() + " files in a single commit: " + newCommit.getHtmlUrl());
    }
    public static void commitDeletedFiles(Map<String, String> filesContent, String gitToken, String repoOwner, String repoName, String branchName, String movieNameWithoutSpaces) throws IOException {

        GitHub github = new GitHubBuilder().withOAuthToken(gitToken).build();
        GHRepository repo = github.getUser(repoOwner).getRepository(repoName);
        // 1. Get the SHA of the latest commit on the target branch
        GHBranch branch = repo.getBranch(branchName);
        String baseCommitSha = branch.getSHA1();

        // 2. Get a tree builder object with the base of the current branch
        GHTreeBuilder treeBuilder = repo.createTree().baseTree(baseCommitSha);

        // 3. Add entries for all files to the tree builder
        // The textEntry method acts as an "upsert" (create new or update existing)
        for (Map.Entry<String, String> entry : filesContent.entrySet()) {
            String pathToFile = entry.getKey();
            String contentOfFile = entry.getValue();
            treeBuilder.textEntry(pathToFile, contentOfFile,false);
        }

        treeBuilder.delete("src/pages/"+movieNameWithoutSpaces+".js");

        // 4. Create the new tree on GitHub
        GHTree newTree = treeBuilder.create();
        String newTreeSha = newTree.getSha();

        // 5. Create the commit pointing to the new tree
        GHCommit newCommit = repo.createCommit()
                .message("multifile commit")
                .tree(newTreeSha)
                .parent(baseCommitSha)
                .create();
        GHRef ref = repo.getRef("refs/heads/" + branchName);

        boolean forceUpdate = false;
        ref.updateTo(newCommit.getSHA1(), forceUpdate);

        // 6. Update the branch reference to point to the new commit
        //repo.updateRef("refs/heads/" + branchName, newCommit.getSHA1());

        System.out.println("Successfully committed " + filesContent.size() + " files in a single commit: " + newCommit.getHtmlUrl());
    }
}


