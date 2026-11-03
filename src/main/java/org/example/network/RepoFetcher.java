package org.example.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

import static org.example.cli.CLI.*;

/**
 * Утилитарный класс для получения файла {@code Cargo.toml} из удаленного репозитория.
 * <p>
 * Реализует сетевые запросы с использованием стандартного класса {@link java.net.HttpURLConnection}.
 * Класс не использует сторонние библиотеки.
 * Основная задача — преобразовать стандартный URL GitHub-репозитория в прямую ссылку на "сырой" файл
 * и скачать его содержимое.
 * </p>
 */
public class RepoFetcher {
    /**
     * Загружает содержимое файла {@code Cargo.toml} из указанного репозитория на GitHub.
     * <p>
     * Метод пытается найти файл в ветках {@code master} и {@code main} по очереди.
     * Если файл не найден ни в одной из веток (ошибка 404), выбрасывается исключение.
     * </p>
     *
     * @param repoUrl URL-адрес репозитория на GitHub (например, {@code https://github.com/user/repo}).
     * @return Содержимое файла {@code Cargo.toml} в виде строки.
     * @throws Exception Если произошла сетевая ошибка, ошибка доступа или файл не найден ни в одной из веток.
     */
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

    /**
     * Выполняет HTTP GET-запрос по указанному URL и возвращает тело ответа в виде строки.
     * <p>
     * Метод настраивает таймауты соединения и чтения (5 секунд).
     * Если сервер возвращает код ответа, отличный от OK, генерируется исключение.
     * </p>
     *
     * @param urlStr Прямой URL-адрес для скачивания файла.
     * @return Содержимое ответа сервера в виде строки.
     * @throws Exception Если произошла ошибка ввода-вывода, таймаут или сервер вернул ошибочный код ответа.
     */
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
