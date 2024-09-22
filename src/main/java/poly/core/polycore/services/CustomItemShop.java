package poly.core.polycore.services;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import poly.core.polycore.PolyCore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomItemShop implements CommandExecutor, Listener {
    private PolyCore plugin;

    // Map of items and their prices the array contains the item and the price
    private Map<String, Map.Entry<ItemStack, Integer>> itemsWithPrices = new HashMap<>();

    public CustomItemShop(PolyCore plugin) {
        this.plugin = plugin;
        ItemStack ironDriver = plugin.customItems.getItem("iron_driver");
        itemsWithPrices.put(ironDriver.getItemMeta().getDisplayName(), Map.entry(ironDriver, 20));

        ItemStack tunnelBore = plugin.customItems.getItem("tunnel_bore");
        itemsWithPrices.put(tunnelBore.getItemMeta().getDisplayName(), Map.entry(tunnelBore, 50));

        ItemStack worldShard = plugin.customItems.getItem("world_shard");
        itemsWithPrices.put(worldShard.getItemMeta().getDisplayName(), Map.entry(worldShard, 15));

        ItemStack fireSword = plugin.customItems.getItem("fire_sword");
        itemsWithPrices.put(fireSword.getItemMeta().getDisplayName(), Map.entry(fireSword, 15));

        ItemStack junkWand = plugin.customItems.getItem("junk_wand");
        itemsWithPrices.put(junkWand.getItemMeta().getDisplayName(), Map.entry(junkWand, 20));

        ItemStack vanishWand = plugin.customItems.getItem("vanish_wand");
        itemsWithPrices.put(vanishWand.getItemMeta().getDisplayName(), Map.entry(vanishWand, 30));

        ItemStack fallPants = plugin.customItems.getItem("fall_pants");
        itemsWithPrices.put(fallPants.getItemMeta().getDisplayName(), Map.entry(fallPants, 25));

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Inventory shop = Bukkit.createInventory(null, 27, ChatColor.GREEN + "Cursed Emerald Shop");

            // Add custom items to the shop inventory
            itemsWithPrices.forEach((name, item) -> {
                ItemMeta meta = item.getKey().getItemMeta();
                List<String> existingLore = meta.getLore();
                // if there is existing lore make sure to add the price to it instead of replacing it
                if (existingLore != null) {
                    existingLore.add(ChatColor.GOLD + "Price: " + ChatColor.WHITE + item.getValue() + " Cursed Emeralds");
                    meta.setLore(existingLore);
                } else {
                    meta.setLore(Arrays.asList(ChatColor.GOLD + "Price: " + ChatColor.WHITE + item.getValue() + " Cursed Emeralds"));
                }
                item.getKey().setItemMeta(meta);
                shop.addItem(item.getKey());
            });

            player.openInventory(shop);
            return true;
        }
        return false;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Find the item by its display name in the map and get the price from the map and check if the player has enough cursed emeralds
        // if they do remove the cursed emeralds and give the player the item they bought. Make sure to check for inventory space
        if (event.getView().getTitle().equals(ChatColor.GREEN + "Cursed Emerald Shop")) {
            event.setCancelled(true);

            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.hasItemMeta()) {
                // Get the display name of the item clicked and get the price from the map
                String displayName = clickedItem.getItemMeta().getDisplayName();
                Map.Entry<ItemStack, Integer> item = itemsWithPrices.get(displayName);
                if (item != null) {
                    Player player = (Player) event.getWhoClicked();
                    int price = item.getValue();

                    if (hasEnoughCustomItems(player, Material.EMERALD, "Cursed Emerald", price)) {
                        removeCustomItems(player, Material.EMERALD, "Cursed Emerald", price);
                        if (player.getInventory().firstEmpty() != -1) {
                            player.getInventory().addItem(item.getKey());
                            player.sendMessage(ChatColor.GREEN + "Purchase successful!");
                        } else {
                            player.sendMessage(ChatColor.RED + "You don't have enough inventory space!");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "You don't have enough Cursed Emeralds!");
                    }
                }

            }
        }
    }

    private boolean hasEnoughCustomItems(Player player, Material material, String displayName, int amount) {
        int count = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == material && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equals(displayName)) {
                count += item.getAmount();
            }
        }
        return count >= amount;
    }

    private void removeCustomItems(Player player, Material material, String displayName, int amount) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == material && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equals(displayName)) {
                int newAmount = item.getAmount() - amount;
                if (newAmount > 0) {
                    item.setAmount(newAmount);
                    break;
                } else {
                    player.getInventory().remove(item);
                    amount -= item.getAmount();
                    if (amount == 0) {
                        break;
                    }
                }
            }
        }
    }
}