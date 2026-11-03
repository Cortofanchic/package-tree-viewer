package org.example.visual;

import org.example.model.DependencyDfs;
import guru.nidi.graphviz.model.MutableGraph;
import static guru.nidi.graphviz.model.Factory.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;

import static org.example.cli.CLI.*;

public class Graphviz {
    public static String exportToDot(DependencyDfs graph) {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph Dependencies {\n");
        sb.append("    rankdir=LR;\n");
        sb.append("    node [shape=box, style=rounded, fontname=\"Helvetica\"];\n");
        sb.append("    edge [fontname=\"Helvetica\"];\n\n");

        for (String node : graph.getAllNodes()) {
            List<String> deps = graph.getDependencies(node);
            if (deps.isEmpty()) {
                sb.append(String.format("    \"%s\";\n", node));
            } else {
                for (String dep : deps) {
                    sb.append(String.format("    \"%s\" -> \"%s\";\n", node, dep));
                }
            }
        }
        sb.append("}\n");
        return sb.toString();
    }
}
