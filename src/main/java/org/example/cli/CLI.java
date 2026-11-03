package org.example.cli;

public class CLI {
    public static void exitWithError(String error){
        System.out.println("[ERROR] " + error);
        System.exit(1);
    }

    public static void printInfo(String info){
        System.out.println("[INFO] " + info);
    }

    public static void printExc(String exc){
        System.out.println("[EXCEPTION] " + exc);
    }

    public static void printClear(String str){
        System.out.println(str);
    }
}
