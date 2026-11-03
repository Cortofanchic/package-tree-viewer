package org.example.visual;

import org.example.model.DependencyDfs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.example.cli.CLI.*;

/**
 * Утилитарный класс для визуализации графа зависимостей в виде текстового (ASCII) дерева.
 * <p>
 * Класс рекурсивно обходит граф и формирует красивую структуру с использованием
 * символов псевдографики ({@code |-}, {@code '-}) для отображения иерархии.
 * Поддерживает ограничение глубины, фильтрацию узлов и корректно обрабатывает циклы.
 * </p>
 */
public class AsciiTree {
    /**
     * Запускает процесс вывода дерева зависимостей в консоль.
     *
     * @param graph      Граф зависимостей.
     * @param root       Имя корневого пакета, с которого начинается построение.
     * @param maxDepth   Максимальная глубина отображения. Если равно -1, ограничений нет.
     * @param excludeSub Подстрока для исключения узлов. Может быть {@code null} или пустой.
     */
    public static void printTree(DependencyDfs graph, String root, int maxDepth, String excludeSub) {
        printClear("<<ASCII Dependency Tree (root: " + root + ")>>");
        Set<String> visited = new HashSet<>();
        printNode(graph, root, "", true, 0, maxDepth, excludeSub, visited);
    }

    /**
     * Рекурсивно выводит узел и его дочерние зависимости с правильным форматированием.
     * <p>
     * Метод определяет, является ли узел последним среди своих "братьев" (для выбора символа ветвления),
     * формирует префикс отступа и рекурсивно вызывает себя для каждой дочерней зависимости.
     * </p>
     *
     * @param graph        Граф зависимостей.
     * @param node         Текущий узел для вывода.
     * @param prefix       Строка отступа, накопленная от родительских уровней.
     * @param isLast       Флаг, указывающий, является ли узел последним среди своих братьев.
     * @param currentDepth Текущая глубина (уровень вложенности).
     * @param maxDepth     Максимальная глубина отображения.
     * @param excludeSub   Подстрока для исключения узлов.
     * @param visited      Множество уже посещенных узлов для защиты от циклов.
     */
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
