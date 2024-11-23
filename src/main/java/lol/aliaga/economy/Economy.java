package lol.aliaga.economy;

import lol.aliaga.economy.commands.BalanceCommand;
import lol.aliaga.economy.commands.PayCommand;
import lol.aliaga.economy.database.DatabaseManager;
import lol.aliaga.economy.listeners.PlayerJoinListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Economy extends JavaPlugin {

    private static Economy instance;
    private DatabaseManager databaseManager;

    public static Economy getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        getLogger().info("Economy plugin enabled");
        instance = this;

        saveDefaultConfig();

        FileConfiguration config = getConfig();
        String host = config.getString("database.host", "localhost");
        int port = config.getInt("database.port", 5432);
        String dbName = config.getString("database.name", "economy");
        String user = config.getString("database.user", "postgres");
        String password = config.getString("database.password", "123");
        int maxPoolSize = config.getInt("database.maxPoolSize", 10);


        databaseManager = new DatabaseManager(host,
                port,
                dbName,
                user,
                password,
                maxPoolSize);

        databaseManager.initializeTables();

        getCommand("balance").setExecutor(new BalanceCommand());
        getCommand("pay").setExecutor(new PayCommand());

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("Economy plugin disabled");

        databaseManager.close();
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
