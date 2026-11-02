package org.example.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

import static org.example.cli.CLI.*;

public class RepoFetcher {
    public static String fetchCargoToml(String repoUrl) throws Exception {
        String[] branches = {"master", "main"};

        if (repoUrl.startsWith("https://github.com/")) {
            String path = repoUrl.replace("https://github.com/", "");
            if (path.endsWith("/")) path = path.substring(0, path.length() - 1);

            for (String branch : branches) {
                String rawUrl = "https://raw.githubusercontent.com/" + path + "/" + branch + "/Cargo.toml";
                printInfo("Trying: " + rawUrl);

                try {
                    return downloadUrl(rawUrl);
                } catch (RuntimeException e) {
                    if (!e.getMessage().contains("404")) {
                        throw e;
                    }
                }
            }
        }
        throw new RuntimeException("Could not find Cargo.toml in master or main branches.");
    }

    private static String downloadUrl(String urlStr) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(urlStr).openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        if (connection.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
            throw new RuntimeException("File not found (404) at " + urlStr);
        } else if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("Failed to fetch. HTTP code: " + connection.getResponseCode());
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            return in.lines().collect(Collectors.joining("\n"));
        }
    }
}
