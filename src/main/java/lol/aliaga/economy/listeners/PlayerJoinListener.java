package lol.aliaga.economy.listeners;

import lol.aliaga.economy.Economy;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String playerUuid = event.getPlayer().getUniqueId().toString();
        String playerName = event.getPlayer().getName();

        Economy.getInstance().getDatabaseManager().executeQuery(
                "SELECT uuid FROM players WHERE uuid = ?",
                resultSet -> {
                    try {
                        if (!resultSet.next()) {
                            Economy.getInstance().getDatabaseManager().executeUpdate(
                                    "INSERT INTO players (uuid, name, money) VALUES (?, ?, ?)",
                                    playerUuid, playerName, 1337.0
                            );
                            Economy.getInstance().getLogger().info("Added new player " + playerName + " to the database.");
                        } else {
                            Economy.getInstance().getDatabaseManager().executeUpdate(
                                    "UPDATE players SET name = ? WHERE uuid = ?",
                                    playerName, playerUuid
                            );
                        }
                    } catch (Exception e) {
                        Economy.getInstance().getLogger().severe("An error occurred while processing player join for " + playerName + ".");
                        e.printStackTrace();
                    }
                    return null;
                },
                playerUuid
        );
    }
}
