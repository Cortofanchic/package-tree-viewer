package org.example.parser;

import org.example.model.DependencyDfs;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import static org.example.cli.CLI.*;

/**
 * Реализует алгоритм обхода графа зависимостей в глубину (Depth-First Search).
 * <p>
 * Главная особенность: алгоритм реализован <b>без использования рекурсии</b>
 * (на основе структуры данных {@link java.util.Stack}).
 * Класс поддерживает ограничение максимальной глубины обхода, фильтрацию узлов по подстроке
 * и корректно обрабатывает циклические зависимости.
 * </p>
 */
public class DfsParser {
    /**
     * Запускает процесс обхода графа в глубину от заданного стартового узла.
     * <p>
     * Метод инициализирует стек и запускает обработку. Если стартовый узел
     * попадает под фильтр (содержит подстроку {@code sub}), обход не выполняется.
     * </p>
     *
     * @param graph     Граф зависимостей, представленный в виде списка смежности.
     * @param maxDepth  Максимальная глубина обхода. Если значение меньше или равно 0,
     *                  обход может быть ограничен только стартовым узлом.
     * @param sub       Подстрока для фильтрации. Узлы, содержащие эту подстроку,
     *                  будут исключены из анализа. Может быть {@code null} или пустой.
     * @param startNode Имя пакета, с которого начинается обход.
     */
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

    /**
     * Основной цикл обработки стека.
     * <p>
     * Извлекает узлы из стека, отслеживает посещенные узлы (для защиты от циклов),
     * выводит их на экран с отступом (пропорционально глубине) и добавляет
     * в стек дочерние зависимости, если не достигнута максимальная глубина.
     * </p>
     *
     * @param stack    Стек, содержащий узлы и их текущую глубину.
     * @param maxDepth Максимально допустимая глубина обхода.
     * @param sub      Подстрока для исключения узлов.
     * @param graph    Граф зависимостей.
     */
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

    /**
     * Создает строку отступа для визуального форматирования вывода.
     * <p>
     * Каждый уровень глубины добавляет два символа {@code ..}.
     * </p>
     *
     * @param depth Текущая глубина узла.
     * @return Строка, состоящая из повторяющихся символов {@code ..}.
     */
    private static String createIndent(int depth){
        StringBuilder indentBuilder = new StringBuilder();
        for (int i=0; i < depth; i++) indentBuilder.append("..");
        return indentBuilder.toString();
    }

    /**
     * Вспомогательный класс для хранения пары "имя узла — глубина".
     * <p>
     * Используется в стеке для отслеживания текущего уровня вложенности
     * при обходе графа без рекурсии.
     * </p>
     */
    private static class NodeDepth {
        String name;
        int depth;

        NodeDepth(String name, int depth){
            this.name = name;
            this.depth = depth;
        }
    }
}
