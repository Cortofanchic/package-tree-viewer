package org.example.build;

import org.example.model.Dependency;
import org.example.model.DependencyDfs;
import org.example.network.RepoFetcher;
import org.example.parser.CargoParser;
import org.example.parser.DfsParser;
import org.example.parser.TestRepoParser;
import org.example.visual.AsciiTree;
import org.example.visual.Graphviz;

import static org.example.cli.CLI.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Главная точка входа в приложение (CLI).
 * <p>
 * Класс отвечает за парсинг аргументов командной строки, их валидацию
 * и запуск соответствующего сценария работы:
 * <ul>
 *   <li>Анализ реального репозитория по URL (сбор прямых зависимостей).</li>
 *   <li>Построение и обход графа на основе локального тестового файла (DFS, фильтрация).</li>
 *   <li>Визуализация графа в формате Graphviz (DOT) и в виде ASCII-дерева.</li>
 * </ul>
 * </p>
 */
public class RunCLI {
    /**
     * Хранилище для строковых аргументов командной строки (ключ-значение).
     */
    private static Map<String, String> stringArgs = new HashMap<>();
    /**
     * Максимальная глубина анализа зависимостей (по умолчанию 3).
     */
    private static int depth = 3;

    /**
     * Главный метод, запускающий приложение.
     *
     * @param args Аргументы командной строки, переданные при запуске.
     */
    public static void main(String[] args) {
        parseArgs(args);
        checkStringArgs();
        parseUrl();
        printTree();
    }

    /**
     * Запускает процесс построения и визуализации дерева зависимостей.
     * <p>
     * Если задан флаг {@code --tree}, выводит ASCII-дерево. В противном случае
     * запускает стандартный DFS-обход. Также выводит текстовое представление
     * графа в формате Graphviz (DOT).
     * </p>
     */
    private static void printTree(){
        String pkg = stringArgs.get("--package");
        String sub = stringArgs.get("--sub");
        String repoPath = stringArgs.get("--repo");
        boolean isTree = Boolean.parseBoolean(stringArgs.get("--tree"));
        try {
            DependencyDfs graph = TestRepoParser.parse(repoPath); // Для тестового режима
            printClear("<<Graphviz DOT Representation>>");
            printClear(Graphviz.exportToDot(graph));

            if (isTree && pkg != null) {
                AsciiTree.printTree(graph, pkg, depth, sub);
            } else if (pkg != null) {
                parseGraph();
            }
        } catch (Exception e) {
            printExc(e.getMessage());
        }
    }

    /**
     * Выводит в консоль структуру обратного (инвертированного) графа зависимостей.
     *
     * @param graph Исходный граф зависимостей.
     */
    private static void printReversedGraph(DependencyDfs graph){
        printClear("<<Reversed graph>>");
        graph.getReversedGraph().printGraph();
    }

    /**
     * Загружает тестовый репозиторий и запускает обход графа в глубину (DFS).
     * <p>
     * Используется, когда пользователь указал путь к локальному файлу ({@code --repo}).
     * Проверяет наличие обязательного аргумента {@code --package}.
     * </p>
     */
    private static void parseGraph(){
        String repoPath = stringArgs.get("--repo");
        try {
            if (repoPath != null && !repoPath.isEmpty()) {
                printInfo("Loading test repository from: " + repoPath);
                DependencyDfs graph = TestRepoParser.parse(repoPath);
                graph.printGraph();
                String pkg = stringArgs.get("--package");
                if (pkg == null || pkg.isEmpty()) {
                    printExc("--package is required to start DFS.");
                    return;
                }
                DfsParser.getGraph(graph, depth, stringArgs.get("--sub"), pkg);

//                // temporary function for 4th part
//                printReversedGraph(graph);
            }
        } catch (Exception e) {
            printExc(e.getMessage());
        }
    }

    /**
     * Анализирует реальный репозиторий по URL-адресу.
     * <p>
     * Скачивает {@code Cargo.toml}, парсит прямые зависимости и выводит их на экран.
     * Используется, когда пользователь указал {@code --url}.
     * </p>
     */
    private static void parseUrl(){
        String url = stringArgs.get("--url");
        if (url != null && !url.isEmpty()) {
            printInfo("Analyzing repository: " + url);
            try {
                String toml = RepoFetcher.fetchCargoToml(url);
                List<Dependency> deps = CargoParser.parseDirectDependencies(toml);
                printInfo("Direct dependencies found: " + deps.size());
                for (Dependency dep : deps) {
                    System.out.println("/ " + dep);
                }
            } catch (Exception e) {
                exitWithError("Failed to analyze repository - " + e.getMessage());
            }
        } else {
            printInfo("No --url provided. Skipping dependency analysis.");
        }
    }

    /**
     * Разбирает массив аргументов командной строки и заполняет внутренние структуры.
     * <p>
     * Обрабатывает числовой аргумент {@code --depth} отдельно, преобразуя его в {@code int}.
     * Остальные аргументы сохраняются в {@link #stringArgs} в формате ключ-значение.
     * </p>
     *
     * @param args Массив аргументов командной строки.
     */
    private static void parseArgs(String[] args){
        for (String arg: args){
            if (arg.startsWith("--depth=")){
                try {
                    depth = Integer.parseInt(arg.split("=")[1]);
                    if (depth <= 0) {
                        exitWithError("depth must be > 0");
                    }
                } catch (NumberFormatException e){
                    exitWithError("depth should be integer number greater than zero");
                }
            } else {
                if (arg.startsWith("--tree")){
                    stringArgs.put("--tree", arg.split("=")[1].toLowerCase());
                } else {
                    stringArgs.put(arg.split("=")[0], arg.split("=")[1]);
                }
            }
        }
    }

    /**
     * Выполняет валидацию всех переданных строковых аргументов.
     * <p>
     * Проверяет общее количество аргументов, а также корректность имени пакета,
     * URL/пути к файлу и флага {@code --tree}.
     * </p>
     */
    private static void checkStringArgs(){
        if (stringArgs.size() > 5){
            exitWithError("extra more arguments");
        }
        checkPackageName(stringArgs.get("--package"));
        checkUrlOrRepo(stringArgs.get("--url"), stringArgs.get("--repo"));
        checkTree(stringArgs.get("--tree"));
    }

    /**
     * Проверяет корректность аргумента {@code --package}.
     * <p>
     * Имя пакета не должно быть пустым и не должно содержать пробелов.
     * </p>
     *
     * @param pkg Имя анализируемого пакета.
     */
    private static void checkPackageName(String pkg) {
        if (pkg == null || pkg.trim().isEmpty()) {
            exitWithError("package name argument (PKG) can't be empty");
        }
        assert pkg != null;
        if (pkg.contains(" ")) {
            exitWithError("package name argument (PKG) can't contain space");
        }
    }

    /**
     * Проверяет корректность аргументов {@code --url} и {@code --repo}.
     * <p>
     * Ожидается, что будет передан ровно один из этих аргументов.
     * Также проверяется формат URL и существование/читаемость файла репозитория.
     * </p>
     *
     * @param url  URL-адрес репозитория (может быть {@code null}).
     * @param repo Путь к файлу тестового репозитория (может быть {@code null}).
     */
    private static void checkUrlOrRepo(String url, String repo){
        if ((url == null || url.isEmpty()) && (repo == null || repo.isEmpty())) {
            exitWithError("use URL or REPO arg (but only one of them)");
        }
        if (url != null && !url.isEmpty() && repo != null && !repo.isEmpty()) {
            exitWithError("use URL or REPO arg (only one of them)");
        }

        if (url != null && !url.isEmpty()) {
            if (!url.matches("^https?://.+")) {
                exitWithError("incorrect URL: right format http://... or https://...");
            }
        }

        if (repo != null && !repo.isEmpty()) {
            File repoFile = new File(repo);
            if (!repoFile.exists()) {
                exitWithError("file of test repository don't find by path " + repo);
            }
            if (!repoFile.isFile()) {
                exitWithError("inputted path is not a way to any file");
            }
            if (!repoFile.canRead()) {
                exitWithError("can't read file by path " + repo);
            }
        }
    }

    /**
     * Проверяет корректность аргумента {@code --tree}.
     * <p>
     * Если аргумент передан, его значение должно быть {@code true} или {@code false}
     * (регистр не важен).
     * </p>
     *
     * @param tree Значение аргумента {@code --tree} (может быть {@code null}).
     */
    private static void checkTree(String tree){
        if (tree != null) {
            if (!tree.equalsIgnoreCase("true") && !tree.equalsIgnoreCase("false")) {
                exitWithError("argument tree should be true or false");
            }
        }
    }
}
