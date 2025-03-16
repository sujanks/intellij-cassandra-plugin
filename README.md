# IntelliJ IDEA Cassandra CQL Plugin

A plugin for IntelliJ IDEA Community Edition that provides support for Apache Cassandra CQL (Cassandra Query Language).

## Features

- CQL file type support (*.cql)
- Syntax highlighting for CQL keywords and operators
- Basic code completion for CQL keywords
- Integrated Cassandra connection management
- CQL query execution with visual results display
- Support for User Defined Types (UDT) in query results
- Modern UI with loading indicators and visual feedback
- Connection testing functionality
- Configurable connection settings:
  - Host(s)
  - Port
  - Username/Password
  - Keyspace
  - Local Datacenter

## Installation

1. Download the latest release from the JetBrains Plugin Repository
2. In IntelliJ IDEA, go to Settings/Preferences -> Plugins
3. Click "Install Plugin from Disk" and select the downloaded file
4. Restart IntelliJ IDEA

## Usage

### Connection Setup
1. Open the Cassandra tool window (View -> Tool Windows -> Cassandra)
2. In the Connection tab, enter your Cassandra connection details:
   - Hosts (comma-separated list)
   - Port (default: 9042)
   - Username and Password
   - Keyspace (optional)
   - Local Datacenter
3. Click "Save Connection" to store your settings
4. Click "Test Connection" to verify connectivity

### Executing Queries
1. Switch to the Query tab
2. Enter your CQL query in the editor
3. Click "Execute Query" or use the keyboard shortcut
4. Results will be displayed in a table format below the query editor
5. The table supports:
   - Column resizing
   - Row selection
   - Proper formatting of UDT values
   - Alternating row colors for better readability

## Building from Source

1. Clone the repository
2. Open the project in IntelliJ IDEA
3. Run the Gradle build:
   - On Unix-based systems: `./gradlew buildPlugin`
   - On Windows: `gradlew.bat buildPlugin`
4. The plugin will be built in `build/distributions/`

## Development Requirements

- Java 17 or later
- IntelliJ IDEA Community Edition or Ultimate (2023.3 or later)
- Cassandra Java Driver (included in dependencies)

Note: Gradle installation is not required as the project uses the Gradle Wrapper.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details. 