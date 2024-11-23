package lol.aliaga.economy.commands;

import lol.aliaga.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PayCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("Usage: /pay <player> <amount>");
            return true;
        }

        String targetName = args[0];
        double amount;
        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage("Invalid amount. Please enter a valid number.");
            return true;
        }

        if (amount <= 0) {
            sender.sendMessage("The amount must be greater than zero.");
            return true;
        }

        Player targetPlayer = Bukkit.getPlayerExact(targetName);

        if (targetPlayer != null) {
            processTransaction(sender, targetPlayer.getUniqueId().toString(), targetPlayer.getName(), amount);
        } else {
            Economy.getInstance().getDatabaseManager().executeQuery(
                    "SELECT uuid, name FROM players WHERE name = ?",
                    resultSet -> {
                        if (resultSet.next()) {
                            String targetUuid = resultSet.getString("uuid");
                            String targetRealName = resultSet.getString("name");
                            processTransaction(sender, targetUuid, targetRealName, amount);
                        } else {
                            sender.sendMessage("The player " + targetName + " does not exist in the database.");
                        }
                        return null;
                    },
                    targetName
            );
        }

        return true;
    }

    private void processTransaction(CommandSender sender, String receiverUuid, String receiverName, double amount) {
        if (sender instanceof Player senderPlayer) {
            String senderUuid = senderPlayer.getUniqueId().toString();

            Economy.getInstance().getDatabaseManager().executeQuery(
                    "SELECT money FROM players WHERE uuid = ?",
                    resultSet -> {
                        if (resultSet.next()) {
                            double senderBalance = resultSet.getDouble("money");

                            if (senderBalance < amount) {
                                senderPlayer.sendMessage("Insufficient funds. You only have " + senderBalance + " coins.");
                                return null;
                            }

                            Economy.getInstance().getDatabaseManager().executeUpdate(
                                    "UPDATE players SET money = money - ? WHERE uuid = ?", amount, senderUuid
                            );

                            Economy.getInstance().getDatabaseManager().executeUpdate(
                                    "UPDATE players SET money = money + ? WHERE uuid = ?", amount, receiverUuid
                            );

                            senderPlayer.sendMessage("You sent " + amount + " coins to " + receiverName + ".");
                            Player onlineReceiver = Bukkit.getPlayerExact(receiverName);
                            if (onlineReceiver != null) {
                                onlineReceiver.sendMessage("You received " + amount + " coins from " + senderPlayer.getName() + ".");
                            }
                        } else {
                            senderPlayer.sendMessage("You don't have an account yet. Please try again later.");
                        }
                        return null;
                    },
                    senderUuid
            );
        } else {
            Economy.getInstance().getDatabaseManager().executeUpdate(
                    "UPDATE players SET money = money + ? WHERE uuid = ?", amount, receiverUuid
            );

            sender.sendMessage("You sent " + amount + " coins to " + receiverName + ".");
            Player onlineReceiver = Bukkit.getPlayerExact(receiverName);
            if (onlineReceiver != null) {
                onlineReceiver.sendMessage("You received " + amount + " coins from the console.");
            }
        }
    }
}
