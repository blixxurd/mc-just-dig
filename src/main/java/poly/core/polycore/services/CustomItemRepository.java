package poly.core.polycore.services;
import java.util.HashMap;

public class CustomItemRepository {

    private HashMap<String, CustomItem> items;

    public CustomItemRepository() {
        items = new HashMap<String, CustomItem>();
    }
    public void addItem(String key, CustomItem item) {
        items.put(key, item);
    }

    public CustomItem getItem(String key) {
        return items.get(key);
    }

    public HashMap<String, CustomItem> getAllItems() {
        return items;
    }

    public void removeItem(String key) {
        items.remove(key);
    }

    public boolean containsItem(String key) {
        return items.containsKey(key);
    }
}