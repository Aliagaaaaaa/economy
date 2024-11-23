# EconomyPlugin

## Description

**EconomyPlugin** is a lightweight economy plugin for Minecraft servers, using PostgreSQL as a database. It provides basic features for players to manage their in-game currency, including checking balances and transferring money to others. Now includes configurable database settings via `config.yml`.

---

## Features

- **Database Support**: Uses PostgreSQL for secure and reliable data storage. Database settings can now be configured directly in `config.yml`.
- **Economy Commands**:
    - `/balance`: View your or another player’s balance.
    - `/pay`: Transfer money to another player.
- **New Player Support**: Automatically registers new players with a starting balance.

---

## Requirements

- **Minecraft Server**: Tested only with Spigot 1.21.3.
- **Java**: Version 17 or higher.
- **Database**: PostgreSQL

---

## Installation

1. **Setup PostgreSQL**:
    - Ensure your PostgreSQL server is running and accessible.

2. **Install the Plugin**:
    - Place `EconomyPlugin.jar` in your server’s `plugins` folder.

3. **Edit Configuration**:
    - After starting the server once, a `config.yml` file will be created in the plugin's folder.
    - Open `config.yml` and edit the following section to match your database setup:
      ```yaml
      database:
        host: localhost
        port: 5432
        name: economy
        user: postgres
        password: 123
        maxPoolSize: 10
      ```

4. **Restart the Server**:
    - After saving changes to `config.yml`, restart the server for the changes to take effect.

---

## Commands

- `/balance [player]`: View your or another player's balance.
- `/pay <player> <amount>`: Send money to another player.

---

## Configuration

- `config.yml` includes the following customizable options:
  ```yaml
  database:
    host: (Database host, e.g., localhost)
    port: (Database port, default 5432)
    name: (Database name, e.g., economy)
    user: (Database user, e.g., postgres)
    password: (Database password)
    maxPoolSize: (Maximum database connection pool size, default 10)
  ```

---

## Compiling the Plugin

To compile the plugin from the source code, follow these steps:

1. **Install Prerequisites**:
    - Ensure you have Java 17 or higher installed.
    - Install Maven, a build automation tool for Java.

2. **Clone the Repository**:
    - Clone the source code to your local machine using Git:
      ```bash
      git clone https://github.com/your-repo/EconomyPlugin.git
      cd EconomyPlugin
      ```

3. **Build the Plugin**:
    - Use Maven to compile the code and package it into a `.jar` file:
      ```bash
      mvn clean package
      ```
    - The compiled `.jar` file will be located in the `target/` directory.

4. **Install the Compiled Plugin**:
    - Copy the `EconomyPlugin-1.0-SNAPSHOT.jar` file from the `target/` folder to your server’s `plugins` directory.

5. **Run Your Server**:
    - Start your Minecraft server. The plugin should load automatically.

---

## Credits

- **Author**: Martín Aliaga

Enjoy building your in-game economy! 🎮
