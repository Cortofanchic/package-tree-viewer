package org.example.model;

import java.util.*;

import static org.example.cli.CLI.printClear;

public class DependencyDfs {
    private HashMap<String, List<String>> dependencies = new HashMap<>();

    public List<String> getDependencies(String packageName) {
        return dependencies.getOrDefault(packageName, Collections.emptyList());
    }

    public void creatDependency(String name){
        dependencies.putIfAbsent(name, new ArrayList<>());
    }

    public void putDependency(String name, String dependency){
        creatDependency(name);
        creatDependency(dependency);
        dependencies.get(name).add(dependency);
    }

    public void printGraph(){
        printClear("<Graph Structure>");
        for (Map.Entry<String, List<String>> entry : dependencies.entrySet()) {
            printClear(entry.getKey() + "->" + entry.getValue());
        }
        printClear("<...>");
    }
}
