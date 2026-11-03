# Package Tree Viewer (CLI)

A Java-based CLI tool for analyzing and visualizing the dependency graph of Rust (Cargo) packages.

**Key Constraint:** Built entirely from scratch. **No package managers** (Cargo) and **no third-party libraries** (JSON/TOML parsers, HTTP clients) are used to fetch or parse dependencies. Only standard Java (`java.net`, `java.io`, `java.util`) is utilized.

## Features

The tool is driven by command-line arguments:

| Argument | Description                                            | Required |
| :--- |:-------------------------------------------------------| :--- |
| `PKG=<NAME>` | The root package to analyze (e.g., `serde` or `A`).    | Yes |
| `URL=<URL>` | GitHub repository URL to fetch the real `Cargo.toml`.  | Yes* |
| `REPO=<PATH>` | Path to a local test repository file (Mock mode).      | Yes* |
| `DEPTH=<N>` | Maximum traversal depth (integer > 0).                 | No |
| `SUB=<STR>` | Substring to exclude specific packages from analysis.  | No |
| `TREE=<BOOLEAN>` | Flag to output dependencies as a formatted ASCII tree. | No |

\*Note: You must provide exactly one of `URL` or `REPO`.*

### Test Repository Format
For `REPO` mode, the file must contain graph edges using uppercase Latin letters.
Format: `PACKAGE -> DEPENDENCY1, DEPENDENCY2`.

Example `test-repo.txt`:
```text
A -> B, C
B -> D
C -> D, E
D -> A
```

### Building
* Build via Gradle 
```text
./gradlew.bat run PKG=<NAME> [URL=<URL> | REPO=<PATH>] [TREE=true] [DEPTH=<N>] [SUB=<STR>]
```
* Run via Makefile
```text
make run PKG=<NAME> [URL=<URL> | REPO=<PATH>] [TREE=true] [DEPTH=<N>] [SUB=<STR>]
```