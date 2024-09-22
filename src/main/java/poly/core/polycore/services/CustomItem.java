package poly.core.polycore.services;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class CustomItem extends ItemStack {
    public CustomItem(Material material, String name, List<String> lore, Enchantment enchantment, int level, String effect){
        super(material);
        NamespacedKey key = new NamespacedKey("polycore", "effect");
        ItemMeta meta = getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        meta.addEnchant(enchantment, level, true);
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, effect);
        setItemMeta(meta);
    }

}