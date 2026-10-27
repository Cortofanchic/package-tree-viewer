package org.example.build;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class RunCLI {
    private static Map<String, String> stringArgs = new HashMap<>();
    private static int depth = 3;

    public static void main(String[] args) {
        parseArgs(args);
        checkStringArgs();
    }

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

    private static void checkStringArgs(){
        if (stringArgs.size() > 5){
            exitWithError("extra more arguments");
        }
        checkPackageName(stringArgs.get("--package"));
        checkUrlOrRepo(stringArgs.get("--url"), stringArgs.get("--repo"));
        checkTree(stringArgs.get("--tree"));
    }

    private static void checkPackageName(String pkg) {
        if (pkg == null || pkg.trim().isEmpty()) {
            exitWithError("package name argument (PKG) can't be empty");
        }
        assert pkg != null;
        if (pkg.contains(" ")) {
            exitWithError("package name argument (PKG) can't contain space");
        }
    }

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

    private static void checkTree(String tree){
        if (tree != null) {
            if (!tree.equalsIgnoreCase("true") && !tree.equalsIgnoreCase("false")) {
                exitWithError("argument tree should be true or false");
            }
        }
    }

    private static void exitWithError(String error){
        System.out.println("[ERROR] " + error);
        System.exit(1);
    }
}
