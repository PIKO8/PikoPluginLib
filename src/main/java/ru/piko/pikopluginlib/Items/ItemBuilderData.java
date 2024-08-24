package ru.piko.pikopluginlib.Items;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import ru.piko.pikopluginlib.PikoPlugin;

import java.lang.reflect.Type;
import java.util.List;

public class ItemBuilderData extends AItemBuilderModification {

    private PersistentDataContainer data;
    private final PikoPlugin plugin;
    private final Gson gson = new Gson();

    public ItemBuilderData(PikoPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onModify() {
        data = builder.getMeta().getPersistentDataContainer();
    }

    @Override
    public void onExitModify() {}

    /**
     * Sets a value of any supported type in the PersistentDataContainer of the item.
     *
     * @param <T> The main data type (e.g., String, Integer).
     * @param <Z> The primitive data type associated with T.
     * @param key The key to use for storing the data.
     * @param type The PersistentDataType representing the data type of the value.
     * @param value The value to store.
     * @return The current ItemBuilderData instance.
     */
    public <T, Z> ItemBuilderData setData(String key, PersistentDataType<T, Z> type, Z value) {
        NamespacedKey namespacedKey = new NamespacedKey(plugin, key);
        data.set(namespacedKey, type, value);
        return this;
    }

    /**
     * Gets a value of any supported type from the PersistentDataContainer of the item.
     *
     * @param <T> The main data type (e.g., String, Integer).
     * @param <Z> The primitive data type associated with T.
     * @param key The key to retrieve the data.
     * @param type The PersistentDataType representing the data type of the value.
     * @return The value associated with the given key, or null if not present.
     */
    public <T, Z> Z getData(String key, PersistentDataType<T, Z> type) {
        NamespacedKey namespacedKey = new NamespacedKey(plugin, key);
        return data.get(namespacedKey, type);
    }

    /**
     * Checks if a value with the given key and type exists in the PersistentDataContainer.
     *
     * @param <T> The main data type (e.g., String, Integer).
     * @param <Z> The primitive data type associated with T.
     * @param key The key to check for the presence of the data.
     * @param type The PersistentDataType representing the data type of the value.
     * @return true if the data exists, false otherwise.
     */
    public <T, Z> boolean hasData(String key, PersistentDataType<T, Z> type) {
        NamespacedKey namespacedKey = new NamespacedKey(plugin, key);
        return data.has(namespacedKey, type);
    }

    /**
     * Sets a list of ItemStacks in the PersistentDataContainer as JSON.
     *
     * @param key The key to use for storing the data.
     * @param items The list of ItemStacks to store.
     */
    public void setItemStackList(String key, List<ItemStack> items) {
        String json = gson.toJson(items, new TypeToken<List<ItemStack>>() {}.getType());
        setData(key, PersistentDataType.STRING, json);
    }

    /**
     * Gets a list of ItemStacks from the PersistentDataContainer.
     *
     * @param key The key to retrieve the data.
     * @return The list of ItemStacks, or null if not present.
     */
    public List<ItemStack> getItemStackList(String key) {
        String json = getData(key, PersistentDataType.STRING);
        if (json != null) {
            Type itemListType = new TypeToken<List<ItemStack>>() {}.getType();
            return gson.fromJson(json, itemListType);
        }
        return null;
    }

    /**
     * Checks if a list of ItemStacks exists for the given key.
     *
     * @param key The key to check for the presence of the data.
     * @return true if the data exists, false otherwise.
     */
    public boolean hasItemStackList(String key) {
        return hasData(key, PersistentDataType.STRING);
    }

    /**
     * Sets an ItemStack in the PersistentDataContainer as JSON.
     *
     * @param key The key to use for storing the data.
     * @param item The ItemStack to store.
     */
    public void setItemStack(String key, ItemStack item) {
        String json = gson.toJson(item);
        setData(key, PersistentDataType.STRING, json);
    }

    /**
     * Gets an ItemStack from the PersistentDataContainer.
     *
     * @param key The key to retrieve the data.
     * @return The ItemStack, or null if not present.
     */
    public ItemStack getItemStack(String key) {
        String json = getData(key, PersistentDataType.STRING);
        if (json != null) {
            return gson.fromJson(json, ItemStack.class);
        }
        return null;
    }

    /**
     * Checks if an ItemStack exists for the given key.
     *
     * @param key The key to check for the presence of the data.
     * @return true if the data exists, false otherwise.
     */
    public boolean hasItemStack(String key) {
        return hasData(key, PersistentDataType.STRING);
    }
}