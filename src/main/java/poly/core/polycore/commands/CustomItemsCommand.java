package poly.core.polycore.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import poly.core.polycore.PolyCore;
import poly.core.polycore.services.CustomItemRepository;

public class CustomItemsCommand implements CommandExecutor {
    private CustomItemRepository customItems;
    public CustomItemsCommand(PolyCore plugin) {
        this.customItems = plugin.customItems;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player.");
            return true;
        }

        Player player = (Player) sender;
        Inventory inventory = Bukkit.createInventory(null, 9 * 3, "Custom Items"); // Create a new inventory with 3 rows

        for (String key : customItems.getAllItems().keySet()) {
            ItemStack item = customItems.getItem(key);
            inventory.addItem(item);
        }

        player.openInventory(inventory); // Open the inventory for the player

        return true;
    }
}