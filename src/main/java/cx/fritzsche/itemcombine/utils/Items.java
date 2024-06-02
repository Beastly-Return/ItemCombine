package cx.fritzsche.itemcombine.utils;

import cx.fritzsche.itemcombine.ItemCombine;

import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class Items {
    public static ItemStack createCombined(String name, Integer amount) {
        String materialString = ItemCombine.plugin.getItemsConfig().getString("Items." + name + ".Material");
        if ((name == null) || (materialString == null)) {
            System.out.println("Unable to find item " + name + " in config");
            return null;
        }
        Material material = Material.matchMaterial(materialString);
        if (material == null) {
            System.out.println("Material for " + name + " was not found");
            return null;
        }
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.getPersistentDataContainer().set(ItemCombine.nameKey, PersistentDataType.STRING, name);
        item.setItemMeta(applyData(itemMeta, amount));

        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemMeta applyData(ItemMeta itemMeta, Integer amount) {
        itemMeta.getPersistentDataContainer().set(ItemCombine.amountKey, PersistentDataType.INTEGER, amount);
        String configName = itemMeta.getPersistentDataContainer().get(ItemCombine.nameKey, PersistentDataType.STRING);
        int newLevel = getLevelForAmount(amount);
        if (newLevel == 0) {
            System.out.println("Failed to get level for amount " + amount);
            return null;
        }
        int visibleAmount = amount - ItemCombine.plugin.getConfig().getInt("Config.Levels." + newLevel);
        int visibleNeeded = ItemCombine.plugin.getConfig().getInt("Config.Levels." + (newLevel + 1)) - ItemCombine.plugin.getConfig().getInt("Config.Levels." + newLevel);
        int maxLevel = ItemCombine.plugin.getConfig().getConfigurationSection("Config.Levels").getKeys(false).size();
        int percentage = 0;
        if (newLevel == maxLevel) {
            itemMeta.getPersistentDataContainer().set(ItemCombine.isUpgradeableKey, PersistentDataType.BOOLEAN, Boolean.valueOf(false));
            visibleNeeded = 0;
            visibleAmount = 0;
            percentage = 100;
        } else {
            itemMeta.getPersistentDataContainer().set(ItemCombine.isUpgradeableKey, PersistentDataType.BOOLEAN, Boolean.valueOf(true));
            percentage = (int) ((float) visibleAmount / visibleNeeded * 100.0F);
        }

        String itemName = ItemCombine.plugin.getItemsConfig().getString("Items." + configName + ".Name");

        String displayName = ItemCombine.plugin.getItemsConfig().getString("Items." + configName + ".Levels." + newLevel + ".DisplayName");

        displayName = displayName.replace("{name}", itemName);
        displayName = displayName.replace("{level}", String.valueOf(newLevel));

        List<String> lore = ItemCombine.plugin.getItemsConfig().getStringList("Items." + configName + ".Levels." + newLevel + ".Lore");
        for (int i = 0; i < lore.size(); i++) {
            String line = lore.get(i);
            line = ChatColor.translateAlternateColorCodes('&', line);
            line = line.replace("{totalamount}", String.valueOf(amount));
            line = line.replace("{neededamount}", String.valueOf(visibleNeeded));
            line = line.replace("{visableamount}", String.valueOf(visibleAmount));
            line = line.replace("{level}", String.valueOf(newLevel));
            line = line.replace("{percentage}", percentage + "%");
            line = line.replace("{progressbar}", "§7[" + ProgressBar.getProgressBar(visibleAmount, visibleNeeded, 40, '|', ChatColor.GREEN, ChatColor.GRAY) + "§7]");
            lore.set(i, line);
        }
        for (String attributeString : ItemCombine.plugin.getItemsConfig().getConfigurationSection("Items." + configName + ".Levels." + newLevel + ".Attributes").getKeys(false)) {
            {
                double attributeValue = ItemCombine.plugin.getItemsConfig().getDouble("Items." + configName + ".Levels." + newLevel + ".Attributes." + attributeString + ".Value");
                if (attributeString.equalsIgnoreCase("GENERIC_ATTACK_SPEED")) {
                    attributeValue -= 4.0D;
                }
                Attribute attribute = Attribute.valueOf(attributeString.toUpperCase());
                itemMeta.removeAttributeModifier(attribute);

                List<String> slotNames = ItemCombine.plugin.getItemsConfig().getStringList("Items." + configName + ".Levels." + newLevel + ".Attributes." + attributeString + ".Slot");
                for (String slotName : slotNames) {
                    EquipmentSlot slot = EquipmentSlot.valueOf(slotName.toUpperCase());
                    AttributeModifier attributeModifier = new AttributeModifier(UUID.randomUUID(), attributeString, attributeValue, AttributeModifier.Operation.ADD_NUMBER, slot);
                    itemMeta.addAttributeModifier(attribute, attributeModifier);
                }
            }
        }

        for (String enchantString : ItemCombine.plugin.getItemsConfig().getConfigurationSection("Items." + configName + ".Levels." + newLevel + ".Enchantments").getKeys(false)) {
            Enchantment enchant = Enchantment.getByName(enchantString);
            int level = ItemCombine.plugin.getItemsConfig().getInt("Items." + configName + ".Levels." + newLevel + ".Enchantments." + enchantString);
            itemMeta.addEnchant(enchant, level, true);
        }
        displayName = ChatColor.translateAlternateColorCodes('&', displayName);
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(lore);
        if (ItemCombine.plugin.getConfig().getBoolean("Config.hide-enchantments")) {
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if (ItemCombine.plugin.getConfig().getBoolean("Config.hide-attributes")) {
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }
        itemMeta.setUnbreakable(true);
        itemMeta.setMaxStackSize(1);
        return itemMeta;
    }

    private static int getLevelForAmount(int amount) {
        int level = 0;
        for (String key : ItemCombine.plugin.getConfig().getConfigurationSection("Config.Levels").getKeys(false)) {
            int requiredAmount = ItemCombine.plugin.getConfig().getConfigurationSection("Config.Levels").getInt(key);
            if (amount < requiredAmount) {
                break;
            }
            level = Integer.parseInt(key);
        }
        return level;
    }
}