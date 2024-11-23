package lol.aliaga.economy.commands;

import lol.aliaga.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player player) {
                checkBalance(sender, player.getUniqueId().toString(), player.getName());
            } else {
                sender.sendMessage("Usage: /balance <player>");
            }
        } else {
            if (!sender.hasPermission("economy.balance.others")) {
                sender.sendMessage("You do not have permission to view other players' balances.");
                return true;
            }

            String targetName = args[0];
            Player target = Bukkit.getPlayerExact(targetName);

            if (target != null) {
                checkBalance(sender, target.getUniqueId().toString(), target.getName());
            } else {
                Economy.getInstance().getDatabaseManager().executeQuery(
                        "SELECT uuid, name, money FROM players WHERE name = ?",
                        resultSet -> {
                            if (resultSet.next()) {
                                String targetRealName = resultSet.getString("name");
                                double balance = resultSet.getDouble("money");
                                sender.sendMessage(targetRealName + "'s current balance is: " + balance + " coins.");
                            } else {
                                sender.sendMessage("The player " + targetName + " does not exist in the database.");
                            }
                            return null;
                        },
                        targetName
                );
            }
        }

        return true;
    }

    private void checkBalance(CommandSender sender, String playerUuid, String playerName) {
        Economy.getInstance().getDatabaseManager().executeQuery(
                "SELECT money FROM players WHERE uuid = ?",
                resultSet -> {
                    if (resultSet.next()) {
                        double balance = resultSet.getDouble("money");
                        sender.sendMessage(playerName + "'s current balance is: " + balance + " coins.");
                    } else {
                        sender.sendMessage(playerName + " does not have a balance record in the database.");
                    }
                    return null;
                },
                playerUuid
        );
    }
}
