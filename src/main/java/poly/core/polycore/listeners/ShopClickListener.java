package poly.core.polycore.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import poly.core.polycore.PolyCore;

public class ShopClickListener implements Listener {
    private PolyCore plugin;

    public ShopClickListener(PolyCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(ChatColor.AQUA + "Shop")) {
            event.setCancelled(true);

            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.hasItemMeta() && clickedItem.getItemMeta().hasLore()) {
                Player player = (Player) event.getWhoClicked();
                int price = Integer.parseInt(clickedItem.getItemMeta().getLore().get(0).split(" ")[2]);

                if (player.getInventory().containsAtLeast(new ItemStack(Material.EMERALD), price)) {
                    player.getInventory().removeItem(new ItemStack(Material.EMERALD, price));
                    player.getInventory().addItem(clickedItem);
                    player.sendMessage(ChatColor.GREEN + "Purchase successful!");
                } else {
                    player.sendMessage(ChatColor.RED + "You don't have enough Cursed Emeralds!");
                }
            }
        }
    }
}