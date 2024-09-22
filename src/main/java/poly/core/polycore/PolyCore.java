package poly.core.polycore;

import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;
import poly.core.polycore.commands.CustomItemsCommand;
import poly.core.polycore.listeners.*;

import poly.core.polycore.services.CustomItemManager;
import poly.core.polycore.services.CustomItemRepository;
import poly.core.polycore.services.CustomItemShop;
import poly.core.polycore.tasks.GlobalBlockCounter;
import poly.core.polycore.tasks.ServerTasks;
import poly.core.polycore.world.WorldBorderManager;

import java.util.logging.Logger;

public final class PolyCore extends JavaPlugin {
    public CustomItemRepository customItems;
    public WorldBorderManager worldBorderManager;
    public GlobalBlockCounter blockCounter;

    private CustomItemManager customItemsManager; // Add this line
    public Logger logger = Bukkit.getLogger();

    private CustomItemShop customItemShop; // Add this line
    @Override
    public void onEnable() {
        // Initialize the world border manager
        worldBorderManager = new WorldBorderManager(this);
        worldBorderManager.setupWorldBorder("world");

        // Set up the custom items
        customItemsManager = new CustomItemManager(this);
        customItemsManager.createCustomItems();
        customItems = customItemsManager.getCustomItems();

        // Set up the global block counter (world level system)
        blockCounter = new GlobalBlockCounter(this);


        // Initialize the custom item shop
        customItemShop = new CustomItemShop(this);

        // Set up the event listeners
        getServer().getPluginManager().registerEvents(customItemShop, this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerLoginListener(customItems), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityDamageListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerSleepListener(this), this);

        this.getCommand("customitems").setExecutor(new CustomItemsCommand(this));
        this.getCommand("shop").setExecutor(customItemShop);

        logger.info("PolyCore Started");

    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("PolyCore Stopped.");
    }
}
