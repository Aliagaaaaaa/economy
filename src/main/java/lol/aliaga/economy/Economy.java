package lol.aliaga.economy;

import lol.aliaga.economy.commands.BalanceCommand;
import lol.aliaga.economy.commands.PayCommand;
import lol.aliaga.economy.database.DatabaseManager;
import lol.aliaga.economy.listeners.PlayerJoinListener;
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

        databaseManager = new DatabaseManager("localhost",
                5432,
                "economy",
                "postgres",
                "123",
                10);

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
