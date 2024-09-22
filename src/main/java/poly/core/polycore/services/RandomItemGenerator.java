package poly.core.polycore.services;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import poly.core.polycore.PolyCore;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomItemGenerator {
    private final PolyCore plugin;
    public final List<Material> blocks = new ArrayList<>();
    public final List<Material> consumables = new ArrayList<>();
    public final List<Material> items = new ArrayList<>();
    public final List<Material> allItems = new ArrayList<>();
    private final Random random = new Random();

    public RandomItemGenerator(PolyCore plugin) {
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

    public Material getRandomMaterial(List<Material> materialList) {
        return materialList.get(random.nextInt(materialList.size()));
    }

    public ItemStack getRandomFood(int amount) {
        Material randomMaterial = this.getRandomMaterial(consumables);
        return new ItemStack(randomMaterial, amount);
    }

    public ItemStack getRandomBlock(int amount) {
        Material randomMaterial = this.getRandomMaterial(blocks);
        return new ItemStack(randomMaterial, amount);
    }

    public ItemStack getRandomItem(int amount) {
        Material randomMaterial = this.getRandomMaterial(items);
        return new ItemStack(randomMaterial, amount);
    }
    public ItemStack getRandom(int amount) {
        Material randomMaterial = this.getRandomMaterial(allItems);
        return new ItemStack(randomMaterial, amount);
    }



}
