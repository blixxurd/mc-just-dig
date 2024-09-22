package poly.core.polycore.listeners;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import poly.core.polycore.PolyCore;
import poly.core.polycore.services.CustomItemRepository;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class PlayerInteractListener implements Listener {
    private PolyCore plugin;
    private Random random = new Random();

    public static final NamespacedKey dataKey = new NamespacedKey("polycore", "effect");
    // List of farm animal types
    private final EntityType[] goodGuys = {
            EntityType.PIG,
            EntityType.CHICKEN,
            EntityType.SHEEP,
            EntityType.IRON_GOLEM,
            EntityType.RABBIT,
            EntityType.VILLAGER,
            EntityType.CAT,
            EntityType.COW,
            EntityType.COW,
            EntityType.COW
    };

    private final EntityType[] badGuys = {
            EntityType.ZOMBIE,
            EntityType.SKELETON,
            EntityType.SPIDER,
            EntityType.HUSK,
            EntityType.SLIME,
            EntityType.CAVE_SPIDER,
            EntityType.PHANTOM
    };
    public PlayerInteractListener(PolyCore plugin) {
        this.plugin = plugin;
    }

    private void rollForRare(int odds, Player player) {
        if (random.nextInt(odds) == 1) { // One in 25 chance for tunnel bore
            player.getWorld().dropItemNaturally(player.getLocation(), this.plugin.customItems.getItem("tunnel_bore"));
            player.sendMessage("You hear a strange noise... an epic item has risen from the earth!");
        }
    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        int worldLevel = plugin.getConfig().getInt("blockBreakLevel");
        
        if(item != null) {
            String itemEffect = item.getItemMeta().getPersistentDataContainer().get(dataKey, PersistentDataType.STRING);

            if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (itemEffect != null && itemEffect.equals("world_shard")) {
                    // Simulate eating
                    player.setCooldown(item.getType(), 20); // 20 ticks = 1 second cooldown
                    player.getWorld().playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_2, 1.0F, 1.0F);
                    player.getWorld().strikeLightningEffect(player.getLocation());
                    player.getWorld().spawnEntity(player.getLocation(), badGuys[random.nextInt(badGuys.length)]);
                    player.getWorld().spawnEntity(player.getLocation(), badGuys[random.nextInt(badGuys.length)]);
                    player.getWorld().spawnEntity(player.getLocation(), badGuys[random.nextInt(badGuys.length)]);
                    player.getWorld().spawnEntity(player.getLocation(), badGuys[random.nextInt(badGuys.length)]);
                    player.getWorld().spawnEntity(player.getLocation(), badGuys[random.nextInt(badGuys.length)]);
                    player.getWorld().spawnEntity(player.getLocation(), badGuys[random.nextInt(badGuys.length)]);

                    if(worldLevel < 20) {
                        this.plugin.worldBorderManager.adjustWorldBorder(player.getWorld(), 2);
                    } else if(worldLevel < 40) {
                        this.plugin.worldBorderManager.adjustWorldBorder(player.getWorld(), 4);
                    } else if(worldLevel < 60) {
                        this.plugin.worldBorderManager.adjustWorldBorder(player.getWorld(), 6);
                    } else {
                        this.plugin.worldBorderManager.adjustWorldBorder(player.getWorld(), 8);
                    }

                    rollForRare(50, player);

                    if (item.getAmount() == 1) {
                        player.getInventory().removeItem(item);
                    } else {
                        item.setAmount(item.getAmount() - 1);
                    }

                    player.sendMessage("You rub the world shard, and feel your horizons start to expand... but a dark energy takes hold!");

                    event.setCancelled(true); // Cancel the event to prevent further processing
                }
                if (itemEffect != null && itemEffect.equals("cursed_emerald")) {
                    // Simulate eating
                    player.setCooldown(item.getType(), 20); // 20 ticks = 1 second cooldown
                    player.getWorld().playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_3, 1.0F, 1.0F);
                    player.getWorld().strikeLightningEffect(player.getLocation());
                    player.getWorld().spawnEntity(player.getLocation(), goodGuys[random.nextInt(goodGuys.length)]);

                    if(worldLevel < 20) {
                        this.plugin.worldBorderManager.adjustWorldBorder(player.getWorld(), -1);
                    } else if(worldLevel < 40) {
                        this.plugin.worldBorderManager.adjustWorldBorder(player.getWorld(), -2);
                    } else if(worldLevel < 60) {
                        this.plugin.worldBorderManager.adjustWorldBorder(player.getWorld(), -3);
                    } else {
                        this.plugin.worldBorderManager.adjustWorldBorder(player.getWorld(), -4);
                    }

                    if (item.getAmount() == 1) {
                        player.getInventory().removeItem(item);
                    } else {
                        item.setAmount(item.getAmount() - 1);
                    }

                    rollForRare(50, player);

                    player.sendMessage("You break the Cursed Emerald, and your world shrinks, but you feel a calming presence in its place.");

                    event.setCancelled(true); // Cancel the event to prevent further processing
                }
                if (itemEffect != null && itemEffect.equals("junk_wand")) {
                    // Create a new chest inventory with 27 slots
                    Inventory chest = Bukkit.createInventory(null, 27, ChatColor.AQUA + "Ethereal Junk Chest");
                    player.swingMainHand();
                    player.openInventory(chest);
                }
                if (itemEffect != null && itemEffect.equals("vanish_wand")) {
                    // Convert the target block to air and play a magical sound and effect
                    Block targetBlock = player.getTargetBlock(null, 50);
                    if(targetBlock.getType() != Material.BEDROCK || targetBlock.getType() != Material.AIR) {
                        // Ensure the block is inside the world border
                        if(this.plugin.worldBorderManager.isInsideBorder(targetBlock.getLocation())) {
                            targetBlock.setType(Material.AIR);
                            player.setCooldown(item.getType(), 10); // 20 ticks = 1 second cooldown
                            player.getWorld().playSound(targetBlock.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0F, 1.0F);
                            player.getWorld().spawnParticle(Particle.END_ROD, targetBlock.getLocation(), 50, 0.5, 0.5, 0.5, 0.1);
                            player.swingMainHand();
                        }
                    }
                }
            }
        }

    }
}
