package cx.fritzsche.itemcombine;

import cx.fritzsche.itemcombine.commands.CombineCommand;
import cx.fritzsche.itemcombine.commands.CombineTab;
import cx.fritzsche.itemcombine.events.InventoryClick;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class ItemCombine extends JavaPlugin {
    private FileConfiguration itemsConfig = null;
    private File itemsConfigFile = null;
    public static ItemCombine plugin;
    public static NamespacedKey isUpgradeableKey = null;
    public static NamespacedKey amountKey = null;
    public static NamespacedKey nameKey = null;

    public void onEnable() {
        plugin = this;
        isUpgradeableKey = new NamespacedKey(plugin, "isUpgradeable");
        amountKey = new NamespacedKey(plugin, "amount");
        nameKey = new NamespacedKey(plugin, "name");
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        createItemsConfig();
        loadItemsConfig();
        getCommand("combine").setExecutor(new CombineCommand());
        getCommand("combine").setTabCompleter(new CombineTab());
        Bukkit.getPluginManager().registerEvents(new InventoryClick(), this);
    }

    public void onDisable() {
    }

    private void createItemsConfig() {
        itemsConfigFile = new File(getDataFolder(), "items.yml");
        if (!itemsConfigFile.exists()) {
            itemsConfigFile.getParentFile().mkdirs();
            saveResource("items.yml", false);
        }

    }

    private void loadItemsConfig() {
        itemsConfig = YamlConfiguration.loadConfiguration(itemsConfigFile);
    }

    public void reloadItemsConfig() {
        if (itemsConfigFile == null) {
            itemsConfigFile = new File(getDataFolder(), "items.yml");
        }

        itemsConfig = YamlConfiguration.loadConfiguration(itemsConfigFile);
    }

    public FileConfiguration getItemsConfig() {
        if (itemsConfig == null) {
            loadItemsConfig();
        }

        return itemsConfig;
    }
}
    