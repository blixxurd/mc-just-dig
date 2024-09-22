package poly.core.polycore.world;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class WorldBorderManager {
    private JavaPlugin plugin;
    private Logger logger;

    public WorldBorderManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }

    public void setupWorldBorder(String worldName) {
        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            WorldBorder border = world.getWorldBorder();
            Location spawnLocation = world.getSpawnLocation();
            border.setCenter(spawnLocation);
            loadWorldBorderSize(world);
            border.setDamageAmount(0.1);
            border.setDamageBuffer(0);
            border.setWarningDistance(10);
            border.setWarningTime(15);
            logger.info("World border set for world '" + worldName);
        } else {
            logger.warning("World '" + worldName + "' not found!");
        }
    }

    public void adjustWorldBorder(World world, double adjustment) {
        WorldBorder border = world.getWorldBorder();
        double size = border.getSize();
        if(size + adjustment <= 8) {
            border.setSize(8);
            Bukkit.broadcastMessage("The world cannot shrink anymore than it has already.");
        } else {
            border.setSize(size + adjustment);
        }
        saveWorldBorderSize(world);
    }

    public void saveWorldBorderSize(World world) {
        double borderSize = world.getWorldBorder().getSize();
        FileConfiguration config = plugin.getConfig();
        config.set("worldborder." + world.getName() + ".size", borderSize);
        plugin.saveConfig();
        logger.info("Saved world border size for " + world.getName() + ": " + borderSize);
    }

    public void loadWorldBorderSize(World world) {
        FileConfiguration config = plugin.getConfig();
        String path = "worldborder." + world.getName() + ".size";
        if (config.contains(path)) {
            double borderSize = config.getDouble(path);
            world.getWorldBorder().setSize(borderSize);
            logger.info("Loaded and set world border size for " + world.getName() + ": " + borderSize);
        } else {
            logger.warning("No saved world border size found for " + world.getName());
            world.getWorldBorder().setSize(8);
            saveWorldBorderSize(world);
        }
    }

    public boolean isInsideBorder(Location location) {
        World world = location.getWorld();
        WorldBorder border = world.getWorldBorder();
        double size = border.getSize();
        Location center = border.getCenter();
        double x = location.getX();
        double z = location.getZ();
        double centerX = center.getX();
        double centerZ = center.getZ();
        if (Math.abs(x - centerX) > size / 2 || Math.abs(z - centerZ) > size / 2) {
            return false;
        } else {
            return true;
        }
    }
}