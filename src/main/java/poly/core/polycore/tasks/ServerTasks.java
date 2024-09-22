package poly.core.polycore.tasks;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import poly.core.polycore.PolyCore;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ServerTasks extends BukkitRunnable {
    private final PolyCore plugin;
    private final List<Material> blocks = new ArrayList<>();
    private final List<Material> consumables = new ArrayList<>();
    private final List<Material> items = new ArrayList<>();
    private final Random random = new Random();
    public ServerTasks(PolyCore plugin) {
        this.plugin = plugin;
        for (Material material : Material.values()) {
            String materialSafeName = material.name().toLowerCase();
            // Skip for debug & command items
            if(materialSafeName.contains("command") || materialSafeName.contains("debug")) {
                continue;
            }
            if (material.isBlock()){ // Check if the material is a block
                blocks.add(material);
            } else if(material.isEdible()) {
                consumables.add(material);
            } else if (material.isItem()) {
                items.add(material);
            }
        }
    }

    public Material getRandom(List<Material> materialList) {
        return materialList.get(random.nextInt(materialList.size()));
    }


    @Override
    public void run() {
        // What you want to schedule goes here
        Material randomMaterial = this.getRandom(blocks);
        ItemStack itemStack = new ItemStack(randomMaterial, 1);
        plugin.getServer().getWorld("world").dropItemNaturally(plugin.getServer().getWorld("world").getSpawnLocation(), itemStack);
        plugin.getServer().broadcastMessage("A random block has fallen from the sky near spawn.");
    }

}
