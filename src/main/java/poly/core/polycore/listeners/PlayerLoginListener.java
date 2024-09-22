package poly.core.polycore.listeners;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.ItemStack;
import poly.core.polycore.services.CustomItemRepository;

public class PlayerLoginListener implements Listener {

    private CustomItemRepository customItemsRepository;

    public PlayerLoginListener(CustomItemRepository customItemsRepository) {
        this.customItemsRepository = customItemsRepository;
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Bukkit.broadcastMessage("Welcome to server.");
        // Give the player the Tunnel Bore item
//        player.getInventory().addItem(customItemsRepository.getItem("tunnel_bore"));
//        player.getInventory().addItem(customItemsRepository.getItem("the_flattener"));
//        player.getInventory().addItem(customItemsRepository.getItem("shimmering_gemstone"));
//        player.sendMessage("You have received a Tunnel Bore & A Flattener! Use them wisely.");

        if (!event.getPlayer().hasPlayedBefore()) {
            // Get the world's spawn location
            Location spawnLocation = event.getPlayer().getWorld().getSpawnLocation();

            // GIve the player some basics
            player.getInventory().addItem(new ItemStack(Material.OAK_SAPLING, 1));
            player.getInventory().addItem(new ItemStack(Material.STONE_AXE, 1));
            player.getInventory().addItem(new ItemStack(Material.STONE_SHOVEL, 1));
            player.getInventory().addItem(new ItemStack(Material.STONE_PICKAXE, 1));
            player.getInventory().addItem(new ItemStack(Material.BONE_MEAL, 5));
            player.getInventory().addItem(new ItemStack(Material.LAVA_BUCKET, 1));
            player.getInventory().addItem(new ItemStack(Material.DIRT, 5));

            // Teleport the player to the exact spawn location
            event.getPlayer().teleport(spawnLocation);

            // Optionally send a message to the player
            event.getPlayer().sendMessage("Welcome! You have been teleported to the spawn point!");
        }
    }
}