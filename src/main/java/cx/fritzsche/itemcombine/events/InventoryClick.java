package cx.fritzsche.itemcombine.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryClick implements Listener {

    @EventHandler
    public void inventoryClick(InventoryClickEvent e){
        if(e.getWhoClicked() instanceof Player){
            Player player = (Player) e.getWhoClicked();
            ItemStack clickedItem = e.getCurrentItem();
            ItemStack cursorItem = e.getCursor();
            if(clickedItem != null && cursorItem != null && clickedItem.hasItemMeta() && cursorItem.hasItemMeta()){


            }
        }
    }
}
