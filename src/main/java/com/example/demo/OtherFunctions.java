package com.example.demo;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.nio.file.StandardOpenOption;

public class OtherFunctions {
    public static List<String> findDifferentFiles() {
        String folderpath = "C:\\Mac\\Home\\Documents\\SampleFolder";
        File directory = new File(folderpath);
        File[] files = directory.listFiles();
        List<String> filenamesWithoutExtensions = new ArrayList<>();

        String frontendpath = "C:\\Windows\\System32\\my-app12\\src\\pages";
        File frontdirectory = new File(frontendpath);
        File[] filesfrontend = frontdirectory.listFiles();
        List<String> frontendnamesWithoutExtensions = new ArrayList<>();

        for (File file : files) {
            String name = file.getName();
            int lastIndexOf = name.lastIndexOf(".");
            filenamesWithoutExtensions.add(name.substring(0, lastIndexOf));
        }

        for (File file : filesfrontend) {
            String name = file.getName();
            int lastIndexOf = name.lastIndexOf(".");
            frontendnamesWithoutExtensions.add(name.substring(0, lastIndexOf));
        }

        Collections.sort(frontendnamesWithoutExtensions);
        Collections.sort(filenamesWithoutExtensions);
        List<String> difference = new ArrayList<>(filenamesWithoutExtensions);
        difference.removeAll(frontendnamesWithoutExtensions);
        return difference;
    }

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

    public static void npmStart() {
        File targetDirectory = new File("C:\\Windows\\System32\\my-app12"); // Replace with your desired path
        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", "start", "npm", "start"); // Example command: list directory contents
        processBuilder.directory(targetDirectory);
        try {
            // Start the process
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            System.out.println("Process exited with code: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String readJsFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        }
        return content.toString();
    }

    public static String modifyJsContent(String originalContent, String diffFile) {
        // Example: Replace a specific function name
        String modifiedContent = originalContent.replace("NewFunc", diffFile);
        return modifiedContent;
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

    public static void writeNewPagesFile(String movieName, String name, String repoName, String gitToken, String movieReview) throws IOException {
        String githubToken = gitToken;
        String owner = "imtryingmybest45";
        //String repoName = "frontEndCode";
        String filePath = "src/NewFunc.js";
        String branch = "main"; // Or your target branch

        GitHub github = new GitHubBuilder().withOAuthToken(githubToken).build();
        GHRepository repo = github.getUser(owner).getRepository(repoName);

        GHContent stuff = repo.getFileContent(filePath, branch);
        String s = new String(stuff.read().readAllBytes(), "UTF-8");

        //String s = readJsFile("C:\\Windows\\System32\\my-app12\\src\\NewFunc.js");
        String firstLetter = name.substring(0, 1).toUpperCase();
        String remainingLetters = name.substring(1);
        String capitalizedString = firstLetter + remainingLetters;
        String fileContent = modifyJsContent(s, capitalizedString);

        String replacementSubstring = "<br /> \n <br />";

        // Replace all blank lines with the specified substring
        String modifiedString = movieReview.replaceAll("(?m)^\\s*\\r?\\n", replacementSubstring + "\n");

        String fileContent2 = fileContent.replace("Insert shit here",modifiedString);
        String fileContent3 = fileContent2.replace("Insert movie name here",movieName);

        String filePath2 = "src/pages/"+name+".js";
        String commitMessage = "Add hellYeah";

        repo.createContent(fileContent3, commitMessage, filePath2);
        System.out.println("File '" + filePath + "' added successfully.");

        //Path filePath = Paths.get(fileName);
        //Files.writeString(filePath, ss, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
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
    public static String getOrigName(String repoName, String gitToken) throws IOException {

        String githubToken = gitToken;
        String owner = "imtryingmybest45";
        //String repoName = "frontEndCode";
        String filePath = "src/pages/Home.js";
        String branch = "main"; // Or your target branch

        GitHub github = new GitHubBuilder().withOAuthToken(githubToken).build();
        GHRepository repo = github.getUser(owner).getRepository(repoName);

        GHContent stuff = repo.getFileContent(filePath, branch);
        String origFileCont = new String(stuff.read().readAllBytes(), "UTF-8");

        //String origFileCont = readJsFile(ppathNName);
        int targetLine = findSubstringLines(origFileCont, "</Routes>");
        String[] linesArray = origFileCont.split("\r?\n");
        List<String> origFileContList = new ArrayList<>(Arrays.asList(linesArray));
        int lineline = targetLine - 1;
        String desLine = origFileContList.get(lineline).toString();
        int firstIndex = desLine.indexOf("\"");
        int secondIndex = desLine.indexOf("\"", firstIndex + 1);
        String origName = desLine.substring(firstIndex + 2, secondIndex);
        return origName;
    }
    public static String editRoutesAppFile(String name, String repoName, String gitToken) throws IOException {
        //String origFileCont = readJsFile(ppathNName);
        String githubToken = gitToken;
        String owner = "imtryingmybest45";
        //String repoName = "frontEndCode";
        String filePath = "src/pages/Home.js";
        String branch = "main"; // Or your target branch

        GitHub github = new GitHubBuilder().withOAuthToken(githubToken).build();
        GHRepository repo = github.getUser(owner).getRepository(repoName);

        GHContent stuff = repo.getFileContent(filePath, branch);
        String origFileCont = new String(stuff.read().readAllBytes(), "UTF-8");

        int targetLine = findSubstringLines(origFileCont, "</Routes>");
        String[] linesArray = origFileCont.split("\r?\n");
        List<String> origFileContList = new ArrayList<>(Arrays.asList(linesArray));
        int lineline = targetLine - 1;
        String desLine = origFileContList.get(lineline).toString();
        int firstIndex = desLine.indexOf("\"");
        int secondIndex = desLine.indexOf("\"", firstIndex + 1);
        String origName = desLine.substring(firstIndex + 2, secondIndex);
        String newContentLine = desLine.replace(origName, name);
        origFileContList.add(targetLine, newContentLine);
        String newContent = String.join("\n", origFileContList);
        return newContent;
    }

    public static String editLinksAppFile(String name, String origName, String newContentRev1, String nextString) throws IOException {
        int targetLine = findSubstringLines(newContentRev1, "return");
        String[] newContRev1StrList = newContentRev1.split("\r?\n");
        List<String> newContRev1StrArrList = new ArrayList<>(Arrays.asList(newContRev1StrList));
        int prevTargetLine = targetLine - 2;
        String desString = newContRev1StrArrList.get(prevTargetLine).toString();
        String newLinkNumber = Integer.toString(getIDNumber(desString));
        String prevLinkNumber = Integer.toString(getIDNumber(desString) - 1);
        desString = desString.replaceFirst(prevLinkNumber, newLinkNumber);
        String origNameWithSpaces = addSpacesToString(origName);
        String newNameWithSpaces = addSpacesToString(name);
        //desString = desString.replaceFirst(origNameWithSpaces, newNameWithSpaces);
        desString = desString.replaceFirst(origNameWithSpaces, nextString);
        desString = desString.replaceFirst(origName, name);

        newContRev1StrArrList.add(prevTargetLine + 1, desString);
        String newContentRev2 = String.join("\n", newContRev1StrArrList);
        return newContentRev2;
    }
    public static String addImportLine(String name, String newContentRev2) throws IOException {
        String desLine = "import " + name + " from " + "'./" + name + "';";
        String[] newContentRev3Str = newContentRev2.split("\r?\n");
        List<String> newContentRev3List= new ArrayList<>(Arrays.asList(newContentRev3Str));
        newContentRev3List.add(0, desLine);
        String newContentRev3 = String.join("\n", newContentRev3List);
        return newContentRev3;
    }
    public static void killCmdLine() throws IOException {
        Runtime rt = Runtime.getRuntime();
        String command = "taskkill /F /IM WindowsTerminal.exe";
        rt.exec(command);
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
}
