package cx.fritzsche.itemcombine.commands;

import cx.fritzsche.itemcombine.ItemCombine;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class CombineTab implements TabCompleter {
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> results = new ArrayList();
        if (args.length == 1)
        {
            results.add("give");
            results.add("reload");
        }
        else if (args.length == 2)
        {
            if (args[0].equalsIgnoreCase("give")) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    results.add(player.getName());
                }
            }
        }
        else if (args.length == 3)
        {
            if (args[0].equalsIgnoreCase("give")) {
                for (String item : ItemCombine.plugin.getItemsConfig().getConfigurationSection("Items").getKeys(false)) {
                    results.add(item);
                }
            }
        }
        else if ((args.length == 4) &&
                (args[0].equalsIgnoreCase("give")))
        {
            results.add("<amount>");
        }
        return results;
    }
}