package org.example.parser;

import org.example.model.Dependency;

import java.util.ArrayList;
import java.util.List;

/**
 * Парсер файлов конфигурации Rust (Cargo.toml).
 * <p>
 * Класс реализует простой построчный анализ текста для извлечения информации
 * о прямых зависимостях из секции {@code [dependencies]}.
 * Парсер написан вручную (без использования сторонних TOML-библиотек).
 * </p>
 * <p>
 * Поддерживаются два основных формата записи зависимостей:
 * <ul>
 *   <li>Простая строка: {@code name = "version"}</li>
 *   <li>Таблица (inline table): {@code name = { version = "version", ... }}</li>
 * </ul>
 * </p>
 */
public class CargoParser {
    /**
     * Извлекает список прямых зависимостей из содержимого файла {@code Cargo.toml}.
     * <p>
     * Метод сканирует текст, находит секцию {@code [dependencies]} и парсит строки,
     * содержащие символ {@code =}. Все остальные секции (например, {@code [dev-dependencies]}
     * или {@code [workspace]}) игнорируются.
     * </p>
     *
     * @param tomlContent Сырое текстовое содержимое файла {@code Cargo.toml}.
     * @return Список объектов {@link Dependency}, содержащих имена и версии прямых зависимостей.
     *         Если секция {@code [dependencies]} отсутствует или пуста, возвращается пустой список.
     */
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
