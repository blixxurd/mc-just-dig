package poly.core.polycore.listeners;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import poly.core.polycore.PolyCore;

public class EntityDamageListener implements Listener {
    private PolyCore plugin;

    private static final NamespacedKey dataKey = new NamespacedKey("polycore", "effect");

    public EntityDamageListener(PolyCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item != null) {
                String itemEffect = player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().get(dataKey, PersistentDataType.STRING);
                if (itemEffect != null && itemEffect.equals("one_hit_kill")) {
                    event.setDamage(1000.0); // Set damage to a high value to ensure one-hit kill
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            Player player = (Player) event.getEntity();
            ItemStack pants = player.getInventory().getLeggings();
            if (pants != null && pants.getItemMeta().getPersistentDataContainer().get(dataKey, PersistentDataType.STRING).equals("fall_pants")) {
                event.setCancelled(true); // Cancel the event to prevent fall damage
            }
        }
    }
}