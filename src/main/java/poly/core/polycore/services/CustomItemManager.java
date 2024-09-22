package poly.core.polycore.services;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class CustomItemManager {
    private CustomItemRepository customItems;
    private JavaPlugin plugin;

    public CustomItemManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.customItems = new CustomItemRepository();
    }

    public void createCustomItems() {
        // Create the Tunnel Bore item
        List<String> boreLore = new ArrayList<String>();
        boreLore.add("This is a powerful tool that can dig a 3x3 tunnel.");
        CustomItem tunnelBore = new CustomItem(Material.DIAMOND_PICKAXE, "Tunnel Bore", boreLore, Enchantment.DIG_SPEED, 5, "tunnel:3x3");
        customItems.addItem("tunnel_bore", tunnelBore);

        // Create the Tunnel Bore item
        List<String> ironDriverLore = new ArrayList<String>();
        ironDriverLore.add("An Iron Pickaxe that can dig a deep 2x1 tunnel.");
        CustomItem ironDriver = new CustomItem(Material.IRON_PICKAXE, "Iron Driver", ironDriverLore, Enchantment.DIG_SPEED, 5, "tunnel:deep:2x1");
        customItems.addItem("iron_driver", ironDriver);

        // Create the Flattener item
        List<String> flattenerLore = new ArrayList<String>();
        flattenerLore.add("A shovel that removes all blocks within a 4x4 area.");
        CustomItem theFlattener = new CustomItem(Material.DIAMOND_SHOVEL, "The Flattener", flattenerLore, Enchantment.DIG_SPEED, 5, "flatten:4x4");
        customItems.addItem("the_flattener", theFlattener);

        // Create the Cursed Emerald item
        List<String> gemLore = new ArrayList<String>();
        gemLore.add("An emerald that contains the power to increase the amount of materials dropped from blocks.");
        gemLore.add("It seems to be cursed.");
        CustomItem shimmeringGemstone = new CustomItem(Material.EMERALD, "Cursed Emerald", gemLore, Enchantment.VANISHING_CURSE, 1, "cursed_emerald");
        customItems.addItem("cursed_emerald", shimmeringGemstone);

        // Create the World Shard item
        List<String> shardLore = new ArrayList<String>();
        shardLore.add("This shard emits a low hum. I think it can help me expand the world boundary.");
        shardLore.add("I should be careful with this.");
        CustomItem worldShard = new CustomItem(Material.AMETHYST_SHARD, "World Shard", shardLore, Enchantment.VANISHING_CURSE, 1, "world_shard");
        customItems.addItem("world_shard", worldShard);

        // Create the sword of fire and fury
        List<String> swordFireLore = new ArrayList<String>();
        swordFireLore.add("A sword that burns with");
        swordFireLore.add("the fury of a thousand suns.");
        CustomItem swordFire = new CustomItem(Material.IRON_SWORD, "Sword of Fire and Fury", swordFireLore, Enchantment.FIRE_ASPECT, 2, "one_hit_kill");
        customItems.addItem("fire_sword", swordFire);

        // Create the bow of the night
        List<String> bowNightLore = new ArrayList<String>();
        bowNightLore.add("A cool bow that does extra damage.");
        CustomItem bowNight = new CustomItem(Material.BOW, "Bow of the Night", bowNightLore, Enchantment.ARROW_DAMAGE, 2, "one_hit_kill");
        customItems.addItem("night_bow", bowNight);

        // Create the wand of storage
        List<String> wandStorageLore = new ArrayList<String>();
        wandStorageLore.add("Summons a chest that destroys");
        wandStorageLore.add("anything you put in it.");
        CustomItem wandStorage = new CustomItem(Material.STICK, "Junky Wand", wandStorageLore, Enchantment.VANISHING_CURSE, 1, "junk_wand");
        customItems.addItem("junk_wand", wandStorage);

        // Create the pants of the unfallen idiot
        List<String> pantsTravelerLore = new ArrayList<String>();
        pantsTravelerLore.add("These once belonged to a");
        pantsTravelerLore.add("clumsy traveler who fell");
        pantsTravelerLore.add("off a cliff and survived.");
        CustomItem pantsTraveler = new CustomItem(Material.LEATHER_LEGGINGS, "Pants of the Unfallen Idiot", pantsTravelerLore, Enchantment.PROTECTION_FALL, 10, "fall_pants");
        customItems.addItem("fall_pants", pantsTraveler);

        // Create the wand of vanishing
        List<String> wandVanishLore = new ArrayList<String>();
        wandVanishLore.add("Casts a spell that makes");
        wandVanishLore.add("things disappear.");
        CustomItem wandVanish = new CustomItem(Material.STICK, "Vanishing Wand", wandVanishLore, Enchantment.VANISHING_CURSE, 1, "vanish_wand");
        customItems.addItem("vanish_wand", wandVanish);
    }

    public CustomItemRepository getCustomItems() {
        return this.customItems;
    }
}