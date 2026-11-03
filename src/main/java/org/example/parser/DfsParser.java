package org.example.parser;

import org.example.model.DependencyDfs;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import static org.example.cli.CLI.*;

public class DfsParser {
    static public void getGraph(DependencyDfs graph, int maxDepth, String sub, String startNode){
        printInfo("DFS search starts (depth:" + maxDepth + "; exclude:" + sub + ")");
        if (sub != null && !sub.isEmpty() && startNode.contains(sub)){
            printExc("Start node " + startNode + " is not filtered");
            return;
        }
        Stack<NodeDepth> stack = new Stack<>();
        stack.push(new NodeDepth(startNode, 0));
        processStack(stack, maxDepth, sub, graph);
        printInfo("DFS search is done!");
    }

    private static void processStack(Stack<NodeDepth> stack, int maxDepth, String sub, DependencyDfs graph){
        Set<String> visited = new HashSet<>();
        while (!stack.isEmpty()){
            NodeDepth current = stack.pop();
            String node = current.name;
            int depth = current.depth;
            
            if (visited.contains(node)){
                continue;
            }
            visited.add(node);
            String indent = createIndent(depth);

            printClear(indent + node + "(depth: " + depth + ")");
            if (depth >= maxDepth){
                continue;
            }
            List<String> dependencies = graph.getDependencies(node);
            for (int i = dependencies.size() - 1; i >= 0; i--) {
                String dep = dependencies.get(i);
                if (sub != null && !sub.isEmpty() && dep.contains(sub)) {
                    continue;
                }
                if (!visited.contains(dep)) {
                    stack.push(new NodeDepth(dep, depth + 1));
                }
            }
        }
    }

    private static String createIndent(int depth){
        StringBuilder indentBuilder = new StringBuilder();
        for (int i=0; i < depth; i++) indentBuilder.append("..");
        return indentBuilder.toString();
    }

    private static class NodeDepth {
        String name;
        int depth;

        NodeDepth(String name, int depth){
            this.name = name;
            this.depth = depth;
        }
    }
}
