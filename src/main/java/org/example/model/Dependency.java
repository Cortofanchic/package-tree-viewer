package org.example.model;

/**
 * Модель данных, представляющая одну зависимость пакета.
 * <p>
 * Содержит имя пакета и опциональную информацию о его версии.
 * Используется для хранения и передачи данных о зависимостях в графе.
 * </p>
 */
public class Dependency {
    /**
     * Имя пакета-зависимости.
     */
    private final String name;
    /**
     * Версия пакета. Может быть {@code null} или пустой строкой, если версия не указана.
     */
    private String version;

    /**
     * Создает новый объект зависимости.
     *
     * @param name    Имя пакета. Не должно быть {@code null}.
     * @param version Версия пакета. Может быть {@code null} или пустой строкой.
     */
    public Dependency(String name, String version) {
        this.name = name;
        this.version = version;
    }

    /**
     * Возвращает имя пакета-зависимости.
     *
     * @return Имя пакета.
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает версию пакета-зависимости.
     *
     * @return Версия пакета или {@code null}/пустая строка, если версия не задана.
     */
    public String getVersion() {
        return version;
    }

    /**
     * Устанавливает или обновляет версию пакета-зависимости.
     *
     * @param version Новая версия пакета.
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Возвращает строковое представление зависимости.
     * <p>
     * Формат вывода: {@code name} или {@code name (version: version)},
     * если версия задана и не пуста.
     * </p>
     *
     * @return Строковое представление зависимости.
     */
    @Override
    public String toString() {
        return name + (version != null && !version.isEmpty() ? " (version: " + version + ")" : "");
    }
}
