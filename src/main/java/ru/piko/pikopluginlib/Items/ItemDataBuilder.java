package ru.piko.pikopluginlib.Items;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import ru.piko.pikopluginlib.PikoPlugin;

public class ItemDataBuilder extends AItemBuilder {

    protected PersistentDataContainer data;
    protected final PikoPlugin plugin;

    /**
     * Use it to create a clean instance <br>
     * Используйте для создания чистого экземпляра
     * @param item the item that needs to be edited | для предмета который надо редактировать
     * @param plugin plugin for creating a NamespacedKey | плагин для создания NamespacedKey
     */
    public ItemDataBuilder(@NotNull ItemStack item, @NotNull PikoPlugin plugin) {
        super(item);
        this.plugin = plugin;
        init();
    }

    /**
     * Use it for `to()` from `AItemBuilder` <br>
     * Используйте для метода `to()` из `AItemBuilder`
     *
     * @param plugin - plugin for creating a NamespacedKey | плагин для создания NamespacedKey
     */
    public ItemDataBuilder(@NotNull PikoPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void init() {
        data = meta.getPersistentDataContainer();
    }

    @Override
    public void finish() {}

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
    public <T, Z> @NotNull ItemDataBuilder setData(@NotNull String key, @NotNull PersistentDataType<T, Z> type, Z value) {
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
    public <T, Z> Z getData(@NotNull String key, @NotNull PersistentDataType<T, Z> type) {
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
    public <T, Z> boolean hasData(@NotNull String key, @NotNull PersistentDataType<T, Z> type) {
        NamespacedKey namespacedKey = new NamespacedKey(plugin, key);
        return data.has(namespacedKey, type);
    }

    public <T, Z> ItemDataBuilder removeData(@NotNull String key, @NotNull PersistentDataType<T, Z> type) {
        NamespacedKey namespacedKey = new NamespacedKey(plugin, key);
        data.remove(namespacedKey);
        return this;
    }

    public @NotNull ItemDataBuilder clearData() {
        data.getKeys().forEach(data::remove);
        return this;
    }
}
