package ru.piko.pikopluginlib.Items;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import ru.piko.pikopluginlib.PikoPlugin;

public class ItemBuilderData extends AItemBuilderModification {

    private PersistentDataContainer data;
    private final PikoPlugin plugin;

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
}
