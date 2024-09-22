package poly.core.polycore.listeners;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import poly.core.polycore.PolyCore;

import java.util.Random;

import java.util.ArrayList;
import java.util.Collection;

public class BlockBreakListener implements Listener {
    private static PolyCore plugin;
    private Random random = new Random();
    public BlockBreakListener(PolyCore plugin) {
        this.plugin = plugin;
    }

    public static final NamespacedKey dataKey = new NamespacedKey("polycore", "effect");

    public void dropSpecial(Location blockLocation) {
        if (random.nextInt(1000) == 0) {  // 1 in 650
            // Drop an emerald at the location of the broken block
            blockLocation.getWorld().dropItemNaturally(blockLocation, plugin.customItems.getItem("world_shard"));
        }

        if (random.nextInt(250) == 0) {  // 1 in 500
            // Drop an emerald at the location of the broken block
            blockLocation.getWorld().dropItemNaturally(blockLocation, plugin.customItems.getItem("cursed_emerald"));
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Block block = e.getBlock();
        Collection<ItemStack> drops = block.getDrops();
        Location blockLocation = block.getLocation();
        Material blockType = block.getType();
        Player player = (Player) e.getPlayer(); // Assuming you have access to the player from the event

        ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
        String effect = item.getItemMeta().getPersistentDataContainer().get(dataKey, PersistentDataType.STRING);

        if(effect != null && effect.equals("tunnel:3x3")) {
            e.setDropItems(false);
            int tunnelDelay = 0;
            for (int x = blockLocation.getBlockX() - 1; x <= blockLocation.getBlockX() + 1; x++) {
                for (int y = blockLocation.getBlockY() - 1; y <= blockLocation.getBlockY() + 1; y++) {
                    for (int z = blockLocation.getBlockZ() - 1; z <= blockLocation.getBlockZ() + 1; z++) {
                        final Block relative = block.getWorld().getBlockAt(x, y, z);
                        if (relative.getType() != Material.AIR && relative.getType() != Material.BEDROCK) {
                            Bukkit.getScheduler().runTaskLater(this.plugin, new Runnable() {
                                @Override
                                public void run() {

                                    relative.breakNaturally();
                                    plugin.blockCounter.incrementBlockCount(1);
                                    dropSpecial(blockLocation);
                                }
                            }, tunnelDelay / 5);
                            tunnelDelay++;
                        }
                    }
                }
            }
        }

        if(effect != null && effect.equals("flatten:4x4")){
            e.setDropItems(false);
            int flattenerDelay = 0;
            // Coordinates of the blocks to break (north, south, east, west)
            int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

            for (int[] direction : directions) {
                int x = blockLocation.getBlockX() + direction[0];
                int z = blockLocation.getBlockZ() + direction[1];
                final Block relative = block.getWorld().getBlockAt(x, blockLocation.getBlockY(), z);

                if (relative.getType() != Material.AIR && (relative.getType() == Material.DIRT || relative.getType() == Material.SAND || relative.getType() == Material.GRAVEL || relative.getType() == Material.GRASS_BLOCK)) {
                    Bukkit.getScheduler().runTaskLater(this.plugin, new Runnable() {
                        public void run() {

                            relative.breakNaturally();
                            plugin.blockCounter.incrementBlockCount(1);
                            dropSpecial(blockLocation);
                        }
                    }, flattenerDelay * 5L);
                    flattenerDelay++;
                }
            }
        }

        if(effect != null && effect.equals("tunnel:deep:2x1")) {
            // Created a 2 high tunnel 1 block wide in the direction the player is facing, starting from the block they broke and going 4 blocks in the direction they are facing
            // The second block for the height is the block below the broken block
            // The 5 deep tunnel is created by breaking the blocks in the direction the player is facing
            // Ignore bedrock and air blocks
            // Use the original block's location to start the tunnel
            e.setDropItems(false);
            int tunnelDelay = 0;
            for (int i = 0; i < 3; i++) {
                final Block relative = block.getRelative(player.getFacing(), i);
                final Block relative2 = block.getRelative(player.getFacing(), i).getRelative(0, -1, 0);

                Bukkit.getScheduler().runTaskLater(this.plugin, new Runnable() {
                    @Override
                    public void run() {
                        if (relative.getType() != Material.BEDROCK) {
                            relative.breakNaturally();
                        }
                        if (relative2.getType() != Material.BEDROCK) {
                            relative2.breakNaturally();
                        }
                        dropSpecial(blockLocation);
                        plugin.blockCounter.incrementBlockCount(1);
                    }
                }, tunnelDelay / 5);
                tunnelDelay++;
            }
        }

        // Count "Cursed Emeralds" in the player's inventory
        int cursedEmeraldCount = 0;
        for (ItemStack invItem : player.getInventory().getContents()) {
            if(invItem != null) {
                String invItemEffect = invItem.getItemMeta().getPersistentDataContainer().get(dataKey, PersistentDataType.STRING);
                if (invItemEffect != null && invItemEffect.equals("cursed_emerald")) {
                    cursedEmeraldCount += invItem.getAmount(); // Sum up all Cursed Emeralds
                }
            }
        }

        // Multiply the drops based on the count of "Cursed Emeralds"
        if (cursedEmeraldCount > 0) {
            e.setDropItems(false);
            for (ItemStack loot : drops) {
                int _amt = (loot.getAmount() * cursedEmeraldCount) + 1; // Multiply by the number of Cursed Emeralds on top of original loot
                Material _block = loot.getType();
                ItemStack _newDrop = new ItemStack(_block, _amt);
                if(loot.getType() == Material.COBBLESTONE || loot.getType() == Material.DIRT || loot.getType() == Material.DIORITE || loot.getType() == Material.ANDESITE || loot.getType() == Material.COBBLED_DEEPSLATE || loot.getType() == Material.TUFF || loot.getType() == Material.GRANITE ) {
                    e.getPlayer().getWorld().spawnParticle(Particle.SMALL_FLAME, blockLocation, 30, 0.5, 0.5, 0.5, 0.05);
                } else {
                    e.getPlayer().getWorld().spawnParticle(Particle.COMPOSTER, blockLocation, 30, 0.5, 0.5, 0.5, 0.05);
                    blockLocation.getWorld().dropItemNaturally(blockLocation, _newDrop);
                }
            }
        }

        dropSpecial(blockLocation);

    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        dropSpecial(event.getLocation());
    }

}
