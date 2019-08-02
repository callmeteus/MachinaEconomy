package org.MachinaEconomy.ThePrometeus;

import java.util.Map;
import java.util.UUID;
import org.MachinaEconomy.ThePrometeus.Entities.MachinaConsoleEntity;
import org.MachinaEconomy.ThePrometeus.Entities.MachinaPlayerEntity;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class CommandListener implements Listener, CommandExecutor {
    Main plugin;

    CommandListener(Main a) {
        plugin                      = a;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // Check if it's the plugin command
        if (!cmd.getName().equalsIgnoreCase("money")) {
            return false;
        }
        
        // Create the MachinaEntity
        Entity e                    = sender instanceof Player ? new MachinaPlayerEntity((Player) sender) : new MachinaConsoleEntity();
        
        // If player sent no arguments, then he
        // maybe wants to know their money.
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command.");
                return true;
            }

            double amount           = plugin.bank.getMoney(e);
            
            // Send entity a message with the amount
            sender.sendMessage(plugin.formatMessage("selfAmount", plugin.utils.format(amount)));
        } else {
            switch(args[0]) {
                default: {
                    // Check if have just one argument
                    if (args.length > 1) {
                        sender.sendMessage(plugin.formatMessage("unknownCommand"));
                        return true;
                    }

                    // Get offline player
                    OfflinePlayer p = plugin.getServer().getOfflinePlayer(args[0]);
                    
                    // Check if offline player has played before
                    if (!p.hasPlayedBefore()) {
                        sender.sendMessage(plugin.formatMessage("unknownPlayer", p.getName()));
                        return true;
                    }
                    
                    double amount   = plugin.bank.getMoney(new MachinaPlayerEntity((Player) p));
            
                    // Send entity a message with the amount
                    sender.sendMessage(plugin.formatMessage("playerAmount", p.getName(), plugin.utils.format(amount)));
                }
                
                break;

                case "pay": {
                    // Check if player has permission
                    if (!sender.hasPermission("machinaeconomy.player.pay")) {
                        sender.sendMessage(plugin.getCommand("money").getPermissionMessage());
                        return true;
                    }

                    // Check if has enough arguments
                    if (args.length != 3) {
                        sender.sendMessage(plugin.formatMessage("notEnoughArguments"));
                        return true;
                    }

                    double amount       = Double.parseDouble(args[2]);

                    // Get offline player
                    OfflinePlayer p     = plugin.getServer().getOfflinePlayer(args[1]);

                    // Check if offline player has played before
                    if (!p.isOnline() && !p.hasPlayedBefore()) {
                        sender.sendMessage(plugin.formatMessage("unknownPlayer", p.getName()));
                        return true;
                    }

                    // Check if it's a number
                    if (Double.isNaN(amount)) {
                        sender.sendMessage(plugin.formatMessage("invalidAmount"));
                        return true;
                    }

                    // Check if player have enough money to complete this operation
                    if (!plugin.bank.hasMoreThan(e, amount)) {
                        sender.sendMessage(plugin.formatMessage("notEnoughMoney"));
                        return true;
                    }

                    // Send operation
                    plugin.bank.adjustMoney(new MachinaPlayerEntity((Player) sender), amount * -1);

                    // Do the operation
                    double newAmount    = plugin.bank.adjustMoney(new MachinaPlayerEntity(p.getPlayer()), amount);
                    
                    // Alert the sender
                    sender.sendMessage(plugin.formatMessage("transactionSent", amount, p.getName()));
                    
                    // Check if receiver player is online
                    if (p.isOnline()) {
                        // Alertt him
                        p.getPlayer().sendMessage(plugin.formatMessage("transactionReceived", sender.getName(), amount));
                    }
                }
                
                break;

                case "top": {
                    // Check if player has permission
                    if (!sender.hasPermission("machinaeconomy.player.top")) {
                        sender.sendMessage(plugin.getCommand("money").getPermissionMessage());
                        return true;
                    }
                    
                    int page                    = 1;  

                    // Check if has enough arguments
                    if (args.length == 2) {
                        page                    = Integer.parseInt(args[1]);
                    }
                    
                    Map<String, Integer> top10  = plugin.bank.getTop10(page);

                    // Send the header
                    sender.sendMessage(plugin.formatMessage("top10Header", page));

                    // Iterate over all players
                    for(String player: top10.keySet()) {
                        // Send a single player
                        sender.sendMessage(ChatColor.BOLD + " •" + player + " " + ChatColor.RESET + plugin.utils.format(top10.get(player)));
                    }
                }
                
                break;

                case "set": {
                    // Check if player has permission
                    if (!sender.hasPermission("machinaeconomy.admin.set")) {
                        sender.sendMessage(plugin.getCommand("money").getPermissionMessage());
                        return true;
                    }
                    
                    // Check if has enough arguments
                    if (args.length != 3) {
                        sender.sendMessage(plugin.formatMessage("notEnoughArguments"));
                        return true;
                    }
                    
                    double amount       = Double.parseDouble(args[2]);

                    // Get offline player
                    OfflinePlayer p = plugin.getServer().getOfflinePlayer(args[1]);

                    // Check if offline player has played before
                    if (!p.isOnline() && !p.hasPlayedBefore()) {
                        sender.sendMessage(plugin.formatMessage("unknownPlayer", p.getName()));
                        return true;
                    }

                    // Check if it's a number
                    if (Double.isNaN(amount)) {
                        sender.sendMessage(plugin.formatMessage("invalidAmount"));
                        return true;
                    }

                    // Do the operation
                    double newAmount    = plugin.bank.setMoney(new MachinaPlayerEntity(p.getPlayer()), amount);

                    // Alert the sender
                    sender.sendMessage(plugin.formatMessage("adjustSent", newAmount, p.getName()));
                    
                    // Check if receiver player is online
                    if (p.isOnline()) {
                        // Alertt him
                        p.getPlayer().sendMessage(plugin.formatMessage("adjustReceived", p.getName(), amount));
                    }
                }
                
                break;
            }
        }

        return true;
    }
}
