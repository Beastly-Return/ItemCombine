package cx.fritzsche.itemcombine;

import cx.fritzsche.itemcombine.commands.CombineCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class ItemCombine extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("combine").setExecutor(new CombineCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
