package org.example.build;

import java.util.ArrayList;
import java.util.List;

public class RunCLI {
    private static List<String> stringArgs = new ArrayList<>();
    private static int depth = 3;

    public static void main(String[] args) {
        parseArgs(args);
    }

    private static void parseArgs(String[] args){
        for (String arg: args){
            if (arg.startsWith("--depth=")){
                depth = Integer.parseInt(arg);
            } else {
                stringArgs.add(arg.substring(arg.indexOf("=") + 1));
            }
        }
    }
}
