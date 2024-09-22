package poly.core.polycore.listeners;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import poly.core.polycore.PolyCore;

public class PlayerSleepListener implements Listener {

    private PolyCore plugin;

    public PlayerSleepListener(PolyCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerBedLeave(PlayerBedLeaveEvent event) {
        World world = event.getPlayer().getWorld();
        // Check if the time is day
        if (world.getTime() < 12300) {
            this.plugin.worldBorderManager.adjustWorldBorder(world, 3);
        }
    }
}