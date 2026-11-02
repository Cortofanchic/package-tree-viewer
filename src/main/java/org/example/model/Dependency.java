package org.example.model;

public class Dependency {
    private final String name;
    private String version;

    public Dependency(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return name + (version != null && !version.isEmpty() ? " (version: " + version + ")" : "");
    }
}
