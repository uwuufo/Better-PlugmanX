# PlugManX-Folia

> A fork of [PlugManX](https://github.com/Test-Account666/PlugManX) with added support for **Folia**, **Paper**, **BungeeCord**, and **Velocity**.

PlugManX is a simple, easy to use plugin that lets server admins manage plugins from either in-game or console without the need to restart the server.

## Supported Platforms

| Platform        | Supported |
|-----------------|-----------|
| Bukkit / Spigot | ✅ |
| Paper 1.21+     | ✅ |
| Folia 1.21+     | ✅ |
| BungeeCord      | ✅ |
| Velocity        | ✅ |

## Features

* Enable, disable, restart, load, reload, and unload plugins from in-game or console.
* List plugins alphabetically, with version if specified.
* Get useful information on plugins such as commands, version, author(s), etc.
* Easily manage plugins without having to constantly restart your server.
* List commands a plugin has registered.
* Find the plugin a command is registered to.
* Tab completion for command names and plugin names.
* Dump plugin list with versions to a file.
* Check if a plugin is up-to-date with dev.bukkit.org
* Permissions Support - All commands default to OP.

## Commands

| Command                               | Description                                                       |
|---------------------------------------|-------------------------------------------------------------------|
| /plugman help                         | Show help information.                                            |
| /plugman list [-v]                    | List plugins in alphabetical order. Use "-v" to include versions. |
| /plugman info [plugin]                | Displays information about a plugin.                              |
| /plugman dump                         | Dump plugin names and version to a file.                          |
| /plugman usage [plugin]               | List commands that a plugin has registered.                       |
| /plugman lookup [command]             | Find the plugin a command is registered to.                       |
| /plugman enable [plugin&#124;all]     | Enable a plugin.                                                  |
| /plugman disable [plugin&#124;all]    | Disable a plugin.                                                 |
| /plugman restart [plugin&#124;all]    | Restart (disable/enable) a plugin.                                |
| /plugman load [plugin]                | Load a plugin.                                                    |
| /plugman reload [plugin&#124;all]     | Reload (unload/load) a plugin.                                    |
| /plugman unload [plugin]              | Unload a plugin.                                                  |
| /plugman check [plugin&#124;all] [-f] | Check if a plugin is up-to-date.                                  |

## Permissions

| Permission Node     | Default | Description                           |
|---------------------|---------|---------------------------------------|
| plugman.admin       | OP      | Allows use of all PlugMan commands.   |
| plugman.update      | OP      | Allows user to see update messages.   |
| plugman.help        | OP      | Allow use of the help command.        |
| plugman.list        | OP      | Allow use of the list command.        |
| plugman.info        | OP      | Allow use of the info command.        |
| plugman.dump        | OP      | Allow use of the dump command.        |
| plugman.usage       | OP      | Allow use of the usage command.       |
| plugman.lookup      | OP      | Allow use of the lookup command.      |
| plugman.enable      | OP      | Allow use of the enable command.      |
| plugman.enable.all  | OP      | Allow use of the enable all command.  |
| plugman.disable     | OP      | Allow use of the disable command.     |
| plugman.disable.all | OP      | Allow use of the disable all command. |
| plugman.restart     | OP      | Allow use of the restart command.     |
| plugman.restart.all | OP      | Allow use of the restart all command. |
| plugman.load        | OP      | Allow use of the load command.        |
| plugman.reload      | OP      | Allow use of the reload command.      |
| plugman.reload.all  | OP      | Allow use of the reload all command.  |
| plugman.unload      | OP      | Allow use of the unload command.      |
| plugman.check       | OP      | Allow use of the check command.       |
| plugman.check.all   | OP      | Allow use of the check command.       |

## Configuration

| File       | URL                                                                                                      |
|------------|----------------------------------------------------------------------------------------------------------|
| config.yml | https://github.com/uwuufo/PlugmanX-Folia/blob/main/plugman-core/src/main/resources/config.yml |

## Building

1. **Clone the repository:**
   ```bash
   git clone https://github.com/uwuufo/PlugmanX-Folia.git
   cd PlugmanX-Folia
   ```

2. **Build the project:**
   ```bash
   mvn clean package
   ```

3. **Find the built JAR:**
   `plugman-assembly/target/PlugManX.jar`

## Version Management

PlugManX uses a centralized version property for easy version management across all modules.
Edit the `<plugman.version>` property in the root `pom.xml` to update the version across all modules.

## Developers

How to include PlugManX-Folia with Maven:

```xml
<repositories>
    <repository>
        <id>PlugManX-Folia</id>
        <url>https://raw.githubusercontent.com/uwuufo/PlugmanX-Folia/repository/</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.rylinaux</groupId>
        <artifactId>PlugManX</artifactId>
        <version>${plugman.version}</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

How to include PlugManX-Folia with Gradle:

```groovy
repositories {
    maven {
        name = 'PlugManX-Folia'
        url = 'https://raw.githubusercontent.com/uwuufo/PlugmanX-Folia/repository/'
    }
}
dependencies {
    compileOnly 'com.rylinaux:PlugManX:${plugman.version}'
}
```

## License

This project is a fork of [PlugManX](https://github.com/Test-Account666/PlugManX), originally based on [PlugMan](https://github.com/r-clancy/PlugMan), and is distributed under the same license: [MIT](license/mit/license.txt).