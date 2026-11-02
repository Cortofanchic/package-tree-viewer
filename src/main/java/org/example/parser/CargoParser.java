package org.example.parser;

import org.example.model.Dependency;

import java.util.ArrayList;
import java.util.List;

public class CargoParser {
    public static List<Dependency> parseDirectDependencies(String tomlContent) {
        List<Dependency> dependencies = new ArrayList<>();
        boolean inDependenciesSection = false;

        for (String line : tomlContent.split("\n")) {
            line = line.trim();

            if (line.isEmpty() || line.startsWith("#")) continue;
            if (line.startsWith("[")) {
                inDependenciesSection = line.equals("[dependencies]");
                continue;
            }
            if (inDependenciesSection && line.contains("=")) {
                String[] parts = line.split("=", 2);
                String name = parts[0].trim();
                String value = parts[1].trim();

                String version = "";

                if (value.startsWith("\"")) {
                    version = value.replace("\"", "");
                }
                else if (value.startsWith("{")) {
                    int versionIdx = value.indexOf("version");
                    if (versionIdx != -1) {
                        int quoteStart = value.indexOf('"', versionIdx);
                        int quoteEnd = value.indexOf('"', quoteStart + 1);
                        if (quoteStart != -1 && quoteEnd != -1) {
                            version = value.substring(quoteStart + 1, quoteEnd);
                        }
                    }
                }
                dependencies.add(new Dependency(name, version));
            }
        }
        return dependencies;
    }
}
