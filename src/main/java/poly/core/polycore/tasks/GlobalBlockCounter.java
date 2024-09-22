package poly.core.polycore.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import poly.core.polycore.PolyCore;
import poly.core.polycore.services.RandomItemGenerator;

import java.util.List;
import java.util.logging.Logger;

public class GlobalBlockCounter implements Listener {
    private PolyCore plugin;
    private int globalBlockCount;

    private int level;

    private int nextLevelAt;
    private BossBar globalBossBar;
    private static final int BLOCK_LEVEL_MODIFIER = 15; // Adjust this value as needed

    private int blocksLeft;

    private RandomItemGenerator randomItemGenerator;

    public Logger logger = Bukkit.getLogger();

    public GlobalBlockCounter(PolyCore plugin) {
        this.plugin = plugin;
        this.globalBlockCount = plugin.getConfig().getInt("blockBreakCountTotal", 1);
        this.level = plugin.getConfig().getInt("blockBreakLevel", 1);
        this.nextLevelAt = this.levelUpThreshold();
        this.blocksLeft = getBlocksLeft();
        this.randomItemGenerator = new RandomItemGenerator(plugin);
        setupBossBar();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        logger.info("Block Counter Started");
    }

    private int getBlocksLeft() {
        return this.nextLevelAt - this.globalBlockCount;
    }

    private int levelUpThreshold() {
        return (int) (this.level * BLOCK_LEVEL_MODIFIER * (this.level * 1.1));
    }

    private int startOfLevelThreshold() {
        if (this.level == 1) {
            return 0;  // No blocks needed to start level 1
        } else {
            int previousLevel = this.level - 1;
            return (int) (previousLevel * BLOCK_LEVEL_MODIFIER * (previousLevel * 1.1));
        }
    }

    private int blocksInLevel() {
        return levelUpThreshold() - startOfLevelThreshold();
    }

    private double levelCompletion() {
        return 1.0 - ((double) blocksLeft / blocksInLevel());
    }

    private void setupBossBar() {
        logger.info("Next level - " + this.nextLevelAt);
        globalBossBar = Bukkit.createBossBar(ChatColor.GREEN + "Total Blocks Broken: " + globalBlockCount + ChatColor.AQUA + " World Level: " + this.level + ChatColor.WHITE + " ("+ ChatColor.DARK_PURPLE + this.blocksLeft + ChatColor.WHITE + ")", BarColor.BLUE, BarStyle.SOLID);
        globalBossBar.setProgress(Math.min(1.0, levelCompletion()));
        for (Player player : Bukkit.getOnlinePlayers()) {
            globalBossBar.addPlayer(player);
        }
    }

    private void dropItemAtPlayer(Player player, ItemStack item) {
        player.getWorld().dropItemNaturally(player.getLocation(), item);
    }

    private void levelUpRewards(int newLevel) {
        logger.info("Rewarding players for reaching level: " + newLevel);
        for (Player player : Bukkit.getOnlinePlayers()) {
            switch (newLevel) {
                case 2:
                    player.sendMessage(ChatColor.GREEN + "You've reached Level 2! Enjoy some eggs!");
                    dropItemAtPlayer(player, new ItemStack(Material.EGG, 5));
                    break;
                case 5:
                    player.sendMessage(ChatColor.GREEN + "You've reached Level 5! A bow has been crafted in your honor!");
                    dropItemAtPlayer(player, this.plugin.customItems.getItem("night_bow"));
                    dropItemAtPlayer(player, new ItemStack(Material.ARROW, 64));
                    break;
                case 7:
                    player.sendMessage(ChatColor.GREEN + "You've reached Level 7! A magic wand has been bestowed upon you!");
                    dropItemAtPlayer(player, this.plugin.customItems.getItem("junk_wand"));
                    break;
                case 10:
                    player.sendMessage(ChatColor.GREEN + "You've reached Level 15! Enjoy your new pickaxes!");
                    dropItemAtPlayer(player, this.plugin.customItems.getItem("iron_driver"));
                    dropItemAtPlayer(player, this.plugin.customItems.getItem("iron_driver"));
                    dropItemAtPlayer(player, this.plugin.customItems.getItem("world_shard"));
                    dropItemAtPlayer(player, this.plugin.customItems.getItem("world_shard"));
                    break;
                case 12:
                    player.sendMessage(ChatColor.GREEN + "You've reached Level 12! You've earned some cursed emeralds!");
                    dropItemAtPlayer(player, this.plugin.customItems.getItem("cursed_emerald"));
                    dropItemAtPlayer(player, this.plugin.customItems.getItem("cursed_emerald"));
                    dropItemAtPlayer(player, this.plugin.customItems.getItem("cursed_emerald"));
                    break;
                case 13:
                    player.sendMessage(ChatColor.GREEN + "You've reached Level 12! You find some pants on the ground.");
                    dropItemAtPlayer(player, this.plugin.customItems.getItem("fall_pants"));
                    dropItemAtPlayer(player, this.plugin.customItems.getItem("cursed_emerald"));
                    dropItemAtPlayer(player, this.plugin.customItems.getItem("world_shard"));
                    break;
                case 19:
                    player.sendMessage(ChatColor.GREEN + "A new wand has been crafted for you!");
                    dropItemAtPlayer(player, this.plugin.customItems.getItem("vanish_wand"));
                    break;
                case 20:
                    player.sendMessage(ChatColor.GREEN + "You've reached Level 20! A sword has been crafted in your honor!");
                    dropItemAtPlayer(player, this.plugin.customItems.getItem("fire_sword"));
                    break;
                case 25:
                    player.sendMessage(ChatColor.GREEN + "You've reached Level 5! You've earned some diamond armor!");
                    dropItemAtPlayer(player, new ItemStack(Material.DIAMOND_HELMET));
                    dropItemAtPlayer(player, new ItemStack(Material.DIAMOND_CHESTPLATE));
                    dropItemAtPlayer(player, new ItemStack(Material.DIAMOND_LEGGINGS));
                    dropItemAtPlayer(player, new ItemStack(Material.DIAMOND_BOOTS));
                    break;
                case 27:
                    player.sendMessage(ChatColor.GREEN + "You've reached level 27! You've been granted some world shards.");
                    dropItemAtPlayer(player, this.plugin.customItems.getItem("world_shard"));
                    dropItemAtPlayer(player, this.plugin.customItems.getItem("world_shard"));
                    dropItemAtPlayer(player, this.plugin.customItems.getItem("world_shard"));
                    dropItemAtPlayer(player, this.plugin.customItems.getItem("world_shard"));
                    dropItemAtPlayer(player, this.plugin.customItems.getItem("world_shard"));
                    break;
                case 30:
                    player.sendMessage(ChatColor.GREEN + "Level 30? Nice. Please enjoy some eggs in this trying time.");
                    dropItemAtPlayer(player, new ItemStack(Material.WANDERING_TRADER_SPAWN_EGG, 1));
                    dropItemAtPlayer(player, new ItemStack(Material.VILLAGER_SPAWN_EGG, 1));
                    break;
                case 50:
                    player.sendMessage(ChatColor.GREEN + "Ever seen one of these before?");
                    dropItemAtPlayer(player, this.plugin.customItems.getItem("tunnel_bore"));
                    dropItemAtPlayer(player, this.plugin.customItems.getItem("tunnel_bore"));
                    break;
                default:
                    if(newLevel < 10) {
                        player.sendMessage(ChatColor.GOLD + "You've reached Level " + newLevel + "! You've been granted some Iron");
                        dropItemAtPlayer(player, new ItemStack(Material.IRON_INGOT, newLevel)); // Give iron ingots equal to their current level
                    }
                    else if(newLevel < 15) {
                        player.sendMessage(ChatColor.GOLD + "You've reached Level " + newLevel + "! You've been granted some food.");
                        dropItemAtPlayer(player, new ItemStack(Material.WHEAT, newLevel)); // Give iron ingots equal to their current level
                    }
                    else if(newLevel < 35) {
                        dropItemAtPlayer(player, new ItemStack(Material.GOLD_INGOT, newLevel)); // Give gold ingots equal to their current level
                    } else {
                        dropItemAtPlayer(player, new ItemStack(Material.DIAMOND, 1));
                        dropItemAtPlayer(player, this.plugin.customItems.getItem("world_shard"));
                        dropItemAtPlayer(player, this.plugin.customItems.getItem("cursed_emerald"));
                    }
                    player.sendMessage(ChatColor.GOLD + "You've reached Level " + newLevel + "!");
                    break;
            }
            player.sendMessage(ChatColor.AQUA + "You feel invigorated from your level up!");
            player.sendMessage(ChatColor.AQUA + "Some extra items fall at your feet to help you on your journey.");
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 6000 * newLevel, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 6000 * newLevel, 2));
            dropItemAtPlayer(player, this.randomItemGenerator.getRandomFood(5));
            dropItemAtPlayer(player, this.randomItemGenerator.getRandomBlock(3));
            dropItemAtPlayer(player, this.randomItemGenerator.getRandomItem(1));
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        incrementBlockCount(1);
        logger.info("Level Completion: " + levelCompletion());
        logger.info("Blocks Left: " + this.blocksLeft);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        incrementBlockCount(event.blockList().size());
    }

    public void incrementBlockCount(int count) {
        globalBlockCount += count;
        if (globalBlockCount >= this.nextLevelAt) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(ChatColor.GOLD + "As you level up, you feel your world start to expand.");
                player.giveExp(this.level * 100);
                this.plugin.worldBorderManager.adjustWorldBorder(this.plugin.getServer().getWorld("world"), 0.5);
                levelUpRewards(this.level + 1);
            }
            this.level = this.level + 1;
            plugin.getConfig().set("blockBreakLevel", this.level);
            this.nextLevelAt = this.levelUpThreshold();
            globalBossBar.setProgress(0); // Reset progress
        }
        saveBlockCount();
        this.blocksLeft = this.getBlocksLeft();
        globalBossBar.setTitle(ChatColor.GREEN + "Total Blocks Broken: " + globalBlockCount + ChatColor.AQUA + " World Level: " + this.level + ChatColor.WHITE + " ("+ ChatColor.DARK_PURPLE + this.blocksLeft +  ChatColor.WHITE + ")");
        globalBossBar.setProgress(Math.min(1.0, levelCompletion()));
    }

    private void saveBlockCount() {
        plugin.getConfig().set("blockBreakCountTotal", this.globalBlockCount);
        plugin.saveConfig();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (globalBossBar != null) {
            globalBossBar.addPlayer(player);
        } else {
            setupBossBar(); // This is to handle the edge case where the boss bar might not be initialized
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // If needed, you could remove players from the boss bar here:
        // globalBossBar.removePlayer(event.getPlayer());
    }
}