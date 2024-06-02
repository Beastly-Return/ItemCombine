package cx.fritzsche.itemcombine.commands;

import cx.fritzsche.itemcombine.ItemCombine;
import cx.fritzsche.itemcombine.utils.Items;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CombineCommand implements CommandExecutor {
    String prefix = "§9[ItemCombine]§7 ";

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("give")) {
                if (!sender.hasPermission("itemcombine.give")) {
                    sender.sendMessage(prefix + "You don't have permission to use this command");
                    return false;
                }

                if (args.length > 2) {
                    int amount = 1;
                    if (args.length > 3 && args[3].matches("[0-9]+")) {
                        amount = Integer.parseInt(args[3]);
                    }

                    ItemStack newItem = Items.createCombined(args[2], amount);
                    if (newItem == null) {
                        sender.sendMessage(prefix + "Item not found");
                        return false;
                    }

                    Player targetPlayer = Bukkit.getPlayer(args[1]);
                    if (targetPlayer != null) {
                        targetPlayer.getInventory().addItem(newItem);
                        sender.sendMessage(prefix + "Item given to " + targetPlayer.getName());
                    } else {
                        sender.sendMessage(prefix + "Player not found");
                    }
                } else {
                    sender.sendMessage(prefix + "Usage: /combine give <player> <item> (<amount>)");
                }
            }

            if (args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("itemcombine.reload")) {
                    sender.sendMessage(prefix + "You don't have permission to use this command");
                    return false;
                }

                sender.sendMessage(prefix + "Reloading config");
                ItemCombine.plugin.reloadConfig();
                ItemCombine.plugin.reloadItemsConfig();
            }
        } else {
            sender.sendMessage(prefix + "§8ItemCombine 1.0");
            sender.sendMessage(prefix + "§7 /combine give <player> <item> (<amount>)");
            sender.sendMessage(prefix + "§7 /combine reload");
        }


        return false;
    }
}
