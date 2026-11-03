package org.example.visual;

import org.example.model.DependencyDfs;
import java.util.List;

/**
 * Утилитарный класс для экспорта графа зависимостей на языке описания диаграмм Graphviz (формат DOT).
 * <p>
 * Класс формирует текстовое представление графа (список ребер), которое может быть
 * передано в утилиту Graphviz для генерации изображения (PNG, SVG и т.д.)
 * или открыто в онлайн-редакторах (например, GraphvizOnline).
 * Генерация текста происходит программно без использования сторонних библиотек.
 * </p>
 */
public class Graphviz {
    /**
     * Преобразует граф зависимостей в строку в формате DOT.
     * <p>
     * Метод проходит по всем узлам графа и формирует описание направленного графа
     * ({@code digraph}). Для каждого пакета создаются направленные ребра
     * вида {@code "Пакет1" -> "Пакет2"}. Узлы без зависимостей выводятся как
     * изолированные вершины. Для улучшения читаемости установлены настройки
     * шрифтов и формы узлов.
     * </p>
     *
     * @param graph Граф зависимостей, который необходимо экспортировать.
     * @return Строка, содержащая валидное описание графа на языке DOT.
     */
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
