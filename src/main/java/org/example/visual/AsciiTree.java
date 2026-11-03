package org.example.visual;

import org.example.model.DependencyDfs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.example.cli.CLI.*;

public class AsciiTree {
    public static void printTree(DependencyDfs graph, String root, int maxDepth, String excludeSub) {
        printClear("<<ASCII Dependency Tree (root: " + root + ")>>");
        Set<String> visited = new HashSet<>();
        printNode(graph, root, "", true, 0, maxDepth, excludeSub, visited);
    }

    private static void printNode(DependencyDfs graph, String node, String prefix, boolean isLast,
                                  int currentDepth, int maxDepth, String excludeSub, Set<String> visited) {

        if (excludeSub != null && !excludeSub.isEmpty() && node.contains(excludeSub)) {
            return;
        }
        if (visited.contains(node)) {
            printClear(prefix + (isLast ? "'- " : "|- ") + node + " (cycle)");
            return;
        }
        printClear(prefix + (isLast ? "'- " : "|- ") + node);
        visited.add(node);
        if (maxDepth != -1 && currentDepth >= maxDepth) {
            return;
        }
        List<String> deps = graph.getDependencies(node);
        List<String> filteredDeps = new ArrayList<>();
        for (String d : deps) {
            if (excludeSub == null || excludeSub.isEmpty() || !d.contains(excludeSub)) {
                filteredDeps.add(d);
            }
        }
        String childPrefix = prefix + (isLast ? "    " : "|   ");
        for (int i = 0; i < filteredDeps.size(); i++) {
            boolean isLastChild = (i == filteredDeps.size() - 1);
            printNode(graph, filteredDeps.get(i), childPrefix, isLastChild,
                    currentDepth + 1, maxDepth, excludeSub, visited);
        }
        visited.remove(node);
    }
}
