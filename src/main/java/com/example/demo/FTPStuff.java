package com.example.demo;
import java.io.*;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPSClient;

public class FTPStuff {

    public static String getFTPClient(String filename) throws IOException {
        FTPSClient ftpsClient = new FTPSClient();

        System.setProperty("java.net.useSystemProxies", "true");
        String user = "TryingThisAgain\\$TryingThisAgain";
        String pass = "Gisg2abbEn6xhKY63Rzofzf7beSKvH2rF7tLbLJ72pCM5Zd9rrdcRzMyWCbh";

        String server = "waws-prod-bn1-037.publish.azurewebsites.windows.net";
        int port = 21;

        System.out.println("Connecting to " + server + " on port " + port);
        ftpsClient.connect(server, port);
        System.out.println("Connected");
        ftpsClient.login(user, pass);

        ftpsClient.enterLocalPassiveMode();
        FTPFile[] files = ftpsClient.listFiles();
        System.out.println("Found " + files.length + " files");

        String remoteFilePath = "/site/wwwroot/"+filename+".txt";
        StringBuilder allInputText = new StringBuilder();
        try (InputStream inputStream = ftpsClient.retrieveFileStream(remoteFilePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            if (inputStream == null) {
                System.out.println("File not found or unable to retrieve.");
                // Handle error, e.g., check ftpClient.getReplyCode() and ftpClient.getReplyString()
            }

            String line;
            System.out.println("Content of " + remoteFilePath + ":");
            while ((line = reader.readLine()) != null) {
                allInputText.append(line).append(System.lineSeparator());
            }

            // Complete the transaction after reading the stream
            boolean success = ftpsClient.completePendingCommand();
            if (success) {
                System.out.println("File retrieved successfully.");
            } else {
                System.out.println("Failed to complete file retrieval.");
            }

        } catch (IOException ex) {
            System.out.println("Error reading file: " + ex.getMessage());
            ex.printStackTrace();
        }
        String allStringContent = allInputText.toString();
        return allStringContent;
    }
}
