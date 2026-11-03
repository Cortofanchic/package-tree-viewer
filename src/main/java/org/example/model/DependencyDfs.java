package org.example.model;

import java.util.*;

import static org.example.cli.CLI.printClear;

/**
 * Модель графа зависимостей, представленная в виде списка смежности.
 * <p>
 * Хранит связи между пакетами, где ключом является имя пакета,
 * а значением — список пакетов, от которых он зависит.
 * Предоставляет методы для добавления узлов и связей, а также для
 * построения обратного (инвертированного) графа.
 * </p>
 */
public class DependencyDfs {
    /**
     * Внутренняя структура данных для хранения графа.
     * Ключ — имя пакета, значение — список его прямых зависимостей.
     */
    private HashMap<String, List<String>> dependencies = new HashMap<>();

    /**
     * Возвращает список прямых зависимостей для указанного пакета.
     * <p>
     * Если пакет не найден в графе или не имеет зависимостей,
     * возвращается пустой неизменяемый список.
     * </p>
     *
     * @param packageName Имя пакета, для которого запрашиваются зависимости.
     * @return Список имен пакетов-зависимостей.
     */
    public List<String> getDependencies(String packageName) {
        return dependencies.getOrDefault(packageName, Collections.emptyList());
    }

    /**
     * Создает новый узел (пакет) в графе, если он еще не существует.
     * <p>
     * Инициализирует пустой список зависимостей для данного пакета.
     * </p>
     *
     * @param name Имя пакета, который нужно добавить в граф.
     */
    public void creatDependency(String name){
        dependencies.putIfAbsent(name, new ArrayList<>());
    }

    /**
     * Добавляет направленную связь (зависимость) между двумя пакетами.
     * <p>
     * Если пакет {@code name} или {@code dependency} отсутствуют в графе,
     * они будут созданы автоматически.
     * </p>
     *
     * @param name       Имя пакета, который зависит.
     * @param dependency Имя пакета-зависимости.
     */
    public void putDependency(String name, String dependency){
        creatDependency(name);
        creatDependency(dependency);
        dependencies.get(name).add(dependency);
    }

    /**
     * Создает и возвращает новый граф с обратными (инвертированными) зависимостями.
     * <p>
     * В новом графе, если в исходном была связь {@code A -> B} (A зависит от B),
     * то появится связь {@code B -> A} (B зависит от A).
     * Это используется для поиска пакетов, которые зависят от заданного.
     * </p>
     *
     * @return Новый объект {@code DependencyDfs}, представляющий обратный граф.
     */
    public DependencyDfs getReversedGraph() {
        DependencyDfs reversed = new DependencyDfs();

        for (Map.Entry<String, List<String>> entry : dependencies.entrySet()) {
            String from = entry.getKey();
            reversed.creatDependency(from);

            for (String to : entry.getValue()) {
                reversed.putDependency(to, from);
            }
        }
        return reversed;
    }

    /**
     * Возвращает список всех уникальных узлов (пакетов), присутствующих в графе.
     *
     * @return Список имен всех пакетов в графе.
     */
    public List<String> getAllNodes(){
        return new ArrayList<>(dependencies.keySet());
    }

    /**
     * Выводит текущую структуру графа в консоль в формате списка смежности.
     * <p>
     * Пример вывода: {@code A->[B, C]}.
     * Использует {@link org.example.cli.CLI#printClear(String)} для вывода.
     * </p>
     */
    public void printGraph(){
        printClear("<Graph Structure>");
        for (Map.Entry<String, List<String>> entry : dependencies.entrySet()) {
            printClear(entry.getKey() + "->" + entry.getValue());
        }
        printClear("<...>");
    }
}
