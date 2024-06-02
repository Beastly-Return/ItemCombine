package cx.fritzsche.itemcombine.events;

import cx.fritzsche.itemcombine.ItemCombine;
import cx.fritzsche.itemcombine.utils.Items;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class InventoryClick implements Listener {
    @EventHandler
    public void upgradeItem(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player player = (Player) e.getWhoClicked();
            ItemStack clickedItem = e.getCurrentItem();
            ItemStack cursorItem = e.getCursor();
            if (clickedItem != null && clickedItem.hasItemMeta()) {
                ItemMeta clickedMeta = clickedItem.getItemMeta();
                Boolean isUpgradeableClicked = clickedMeta.getPersistentDataContainer().get(ItemCombine.isUpgradeableKey, PersistentDataType.BOOLEAN);
                Integer amountClicked = clickedMeta.getPersistentDataContainer().get(ItemCombine.amountKey, PersistentDataType.INTEGER);
                String nameClicked = clickedMeta.getPersistentDataContainer().get(ItemCombine.nameKey, PersistentDataType.STRING);
                if ((e.getInventory().getType().equals(InventoryType.ANVIL) || e.getClickedInventory().getType().equals(InventoryType.ENCHANTING)) && amountClicked != null) {
                    e.setCancelled(true);
                    player.sendMessage("You can't use this item in the Anvil or Enchanting! Upgrade it in your Inventory!");
                    return;
                }

                if (cursorItem != null && clickedItem.hasItemMeta() && cursorItem.hasItemMeta()) {
                    ItemMeta cursorMeta = cursorItem.getItemMeta();
                    Boolean isUpgradeableCursor = cursorMeta.getPersistentDataContainer().get(ItemCombine.isUpgradeableKey, PersistentDataType.BOOLEAN);
                    String nameCursor = cursorMeta.getPersistentDataContainer().get(ItemCombine.nameKey, PersistentDataType.STRING);
                    Integer amountCursor = cursorMeta.getPersistentDataContainer().get(ItemCombine.amountKey, PersistentDataType.INTEGER);
                    if (Boolean.TRUE.equals(isUpgradeableClicked) && Boolean.TRUE.equals(isUpgradeableCursor) && nameClicked.equals(nameCursor)) {
                        e.setCancelled(true);
                        Integer combinedAmount = amountClicked + amountCursor;
                        if (ItemCombine.plugin.getConfig().getBoolean("Config.Debug")) {
                            player.sendMessage("Both Items can be Upgraded and are equal type!");
                            player.sendMessage("New amount: " + combinedAmount);
                        }
                        ItemMeta newMeta = Items.applyData(clickedItem.getItemMeta(), combinedAmount);
                        if (newMeta != null) {
                            cursorItem.setAmount(0);
                            clickedItem.setItemMeta(newMeta);
                        } else {
                            player.sendMessage("Something went wrong! Please contact the Admins!");
                        }
                    }
                }
            }
        }

    }
}
    