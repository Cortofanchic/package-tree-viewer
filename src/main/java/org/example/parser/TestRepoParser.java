package org.example.parser;

import org.example.model.DependencyDfs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Парсер локальных файлов для загрузки тестового графа зависимостей.
 * <p>
 * Используется в режиме тестирования (когда вместо реального URL-репозитория
 * указывается путь к файлу). Файл содержит описание графа, где имена пакетов
 * обычно представлены заглавными латинскими буквами.
 * </p>
 * <p>
 * Поддерживаемый формат строки: {@code ПАКЕТ -> ЗАВИСИМОСТЬ1, ЗАВИСИМОСТЬ2}.
 * Строки, начинающиеся с символа {@code #}, считаются комментариями и игнорируются.
 * </p>
 */
public class TestRepoParser {
    public static DependencyDfs parse(String filePath) throws IOException {
        DependencyDfs graph = new DependencyDfs();
        List<String> lines = Files.readAllLines(Paths.get(filePath));

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) continue;

            String[] parts = line.split("->", 2);
            if (parts.length < 2) continue;

            String parent = parts[0].trim();
            graph.creatDependency(parent);

            String[] deps = parts[1].split(",");
            for (String dep : deps) {
                dep = dep.trim();
                if (!dep.isEmpty()) {
                    graph.putDependency(parent, dep);
                }
            }
        }
        return graph;
    }
}
